import ProTable from '@ant-design/pro-table';
import { PageContainer } from '@ant-design/pro-layout';
import { request } from '@@/plugin-request/request';
import ProForm, { ModalForm, ProFormText } from '@ant-design/pro-form';
import { Button, message } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import { useRef, useState } from 'react';
import { Link,history } from 'umi';




const DictList = ()=>{

  // eslint-disable-next-line react-hooks/rules-of-hooks
  const actionRef = useRef();
  // eslint-disable-next-line react-hooks/rules-of-hooks
  const formRef = useRef();
  const [modalVisit, setModalVisit] = useState(false);
  const [formData, setFormData] = useState({});
  const [modalFormTitle,setModalFormTitle] = useState("新建字典类型")

  const columns = [

    {
      title:'字典名称',
      dataIndex:'dictName'
    },
    {
      title:'字典类型',
      dataIndex:'dictType'
    },
    // {
    //   title:'排序',
    //   dataIndex:'sort'
    // },
    {
      title: '操作',
      valueType: 'option',
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      render: (text, record, _, action) => [
        <a
          key="editable"
          onClick={() => {
            setModalFormTitle("编辑字典类型")
            setFormData(record)
            formRef.current.setFieldsValue(record)
            setModalVisit(true)
          }}
        >
          编辑
        </a>,
        <a
          key="view"
          onClick={() => {
            history.push({
              pathname: 'dict/data',
              query: {
                dictType: record.dictType,
              },
            });
          }}
        >

          数据
        </a>,
        <a
          key="view"
          onClick={
            async () => {
              await request("/api/sys/user", {
                method: "DELETE",
                params: { "param": JSON.stringify(record) },
              });
              actionRef.current.reload()
            }
          }
        >
          删除
        </a>,
      ],
    },

  ]
  return(
    <div>
      <PageContainer>
        <ProTable

          actionRef={actionRef}
          columns={columns}
          request={
            // eslint-disable-next-line @typescript-eslint/no-unused-vars
            async (params, sort, filter) => {
              const result = await request("/api/sys/dict/type", {
                params: {param:JSON.stringify(params)}
              });
              return {
                data: result,
                success: true,
              };
            }
          }
          toolBarRender={() => [
            // <Button key="button" icon={<PlusOutlined />} type="primary">
            //   新建
            // </Button>,
            <ModalForm
              formRef={formRef}
              title={modalFormTitle}
              visible={modalVisit}
              onVisibleChange={setModalVisit}
              trigger={
                <Button type="primary" onClick={
                  ()=>{
                    setModalVisit(true)
                    setFormData({})
                    formRef.current.setFieldsValue({
                      dictName:"",
                      dictType:""
                    })
                  }
                }>
                  <PlusOutlined />
                  新建
                </Button>
              }
              onFinish={async (values) => {
                // eslint-disable-next-line no-param-reassign
                values.id = formData.id
                await request("/api/sys/dict/type", {
                  method: "POST",
                  data: { "param": JSON.stringify(values) },
                  requestType: 'form'
                });
                message.success('提交成功');
                actionRef.current.reload()
                return true;
              }}
          >
            <ProForm.Group>
              <ProFormText width="md" name="dictName" label="名称" placeholder="请输入名称" />
              <ProFormText width="md" name="dictType" label="类型" placeholder="请输入编码" />
            </ProForm.Group>

          </ModalForm>
        ]}
        />
      </PageContainer>

    </div>
  )
}

export default DictList
