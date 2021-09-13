import { PageContainer } from '@ant-design/pro-layout';
import ProTable from '@ant-design/pro-table';
import { request } from '@@/plugin-request/request';
import { Button,message  } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import ProForm,{ ModalForm, ProFormText } from '@ant-design/pro-form';
import { useRef, useState } from 'react';



const getDataList = async () => {
  const result = await request("/api/sys/dept", {});
  console.log(result)
  return {
    data: result,
    // success 请返回 true，
    // 不然 table 会停止解析数据，即使有数据
    success: true,
    // 不传会使用 data 的长度，如果是分页一定要传
    // total: number,
  };
}

const save = (values) => {
    request("/api/sys/dept", {
    method: "POST",
    data: { "param": JSON.stringify(values) },
    requestType: 'form'
  });
}

const deptList = ()=>{
  // eslint-disable-next-line react-hooks/rules-of-hooks
  const actionRef = useRef();
  // eslint-disable-next-line react-hooks/rules-of-hooks
  const formRef = useRef();
  const [modalVisit, setModalVisit] = useState(false);
  const [formData, setFormData] = useState({});
  const columns = [

    {
      title:'名称',
      dataIndex:'name'
    },
    {
      title:'编码',
      dataIndex:'code'
    },
    {
      title: '操作',
      valueType: 'option',
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      render: (text, record, _, action) => [
        <a
          key="editable"
          onClick={() => {
            setFormData(record)
            formRef.current.setFieldsValue(record)
            setModalVisit(true)
          }}
        >
          编辑
        </a>,
        <a
          key="view"
          onClick={
            async () => {
              await request("/api/sys/dept", {
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
          request={getDataList}
          toolBarRender={() => [
            // <Button key="button" icon={<PlusOutlined />} type="primary">
            //   新建
            // </Button>,
            <ModalForm
              formRef={formRef}
              title="新建部门"
              visible={modalVisit}
              onVisibleChange={setModalVisit}
              trigger={
                <Button type="primary" onClick={
                  ()=>{
                    setModalVisit(true)
                    setFormData({})
                    formRef.current.setFieldsValue({
                      name:"",
                      code:""
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
                await save(values);
                message.success('提交成功');
                actionRef.current.reload()
                return true;
              }}
            >
              <ProForm.Group>
                <ProFormText width="md" name="name" label="名称" placeholder="请输入名称" />
                <ProFormText width="md" name="code" label="编码" placeholder="请输入编码" />
              </ProForm.Group>

            </ModalForm>
          ]}
        />
      </PageContainer>

    </div>
  )
}

export default deptList
