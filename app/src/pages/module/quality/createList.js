import { PageContainer } from '@ant-design/pro-layout';
import ProTable from '@ant-design/pro-table';
import { request } from '@@/plugin-request/request';
import { Link,history } from 'umi';
import { useRef, useState } from 'react';
import { Button } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import ProForm, { ModalForm, ProFormText } from '@ant-design/pro-form';



const getDataList = async () => {
  const result = await request("/api/quality/form/all", {
    method:"GET"
  });
  return {
    data: result,
    success: true,
  };
}


const createList = ()=>{
  // eslint-disable-next-line react-hooks/rules-of-hooks
  const formRef = useRef();
  // eslint-disable-next-line react-hooks/rules-of-hooks
  const actionRef = useRef();

  const [editData,setEditData] = useState({})

  const [modalVisit, setModalVisit] = useState(false);

  const columns = [

    {
      title:'名称',
      dataIndex:'formName'
    },
    {
      title:'编码',
      dataIndex:'formCode'
    },
    {
      title: '操作',
      valueType: 'option',
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      render: (text, record, _, action) => [
        <a
          key="editable"
          onClick={() => {
            // action?.startEditable?.(record.id);
            history.push({
              pathname: 'form/edit',
              query: {
                formCode: record.formCode,
                formName: record.formName
              },
            });
          }}
        >
          编辑
        </a>,
        <a
          onClick={async () => {
            const param = {
              id: record.id,
            }
            await request("/api/quality/form", {
              method: "DELETE",
              params: { param: JSON.stringify(param) },
            });
            actionRef.current.reload()
          }}
        >
          删除
        </a>,
        <a
          onClick={async () => {
            setEditData(record)
            setModalVisit(true)
            formRef.current.setFieldsValue(record)
          }}
        >
          修改表单
        </a>,

      ],
    },

  ]


  return(
    <div>

      <ModalForm
        formRef={formRef}
        title="编辑表单"
        visible={modalVisit}
        onFinish={async (values) => {
          // eslint-disable-next-line no-param-reassign
          values.id = editData.id
          await request("/api/quality/form", {
            method: "PUT",
            data: { param: JSON.stringify(values) },
            requestType: 'form'
          });
          setModalVisit(false)
          actionRef.current.reload()
        }}
      >
        <ProForm.Group>
          <ProFormText
            width="md"
            name="formName"
            label="表单名称"
          />

          <ProFormText width="md" name="formCode" label="表单编码" />
        </ProForm.Group>
      </ModalForm>

      <PageContainer>
        <ProTable
          actionRef={actionRef}
          columns={columns}
          editable={{
            type: 'multiple',
            onSave: async (rowKey, data, row) => {
              console.log(data)
              try {
                await request("/api/quality/form", {
                  method: "POST",
                  data: { "param": JSON.stringify(data) },
                  requestType: 'form'
                });
                actionRef.current.reload()
              } catch (errorInfo) {
                console.log('Failed:', errorInfo);
              }
            },
          }}
          rowKey="id"
          request={getDataList}
          toolBarRender={() => [
            <Link to="form/new">
              <Button key="button" icon={<PlusOutlined />} type="primary">
                新建
              </Button>,
            </Link>


          ]}
        />
      </PageContainer>

    </div>
  )
}

export default createList
