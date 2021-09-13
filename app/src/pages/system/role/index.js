import ProTable from '@ant-design/pro-table';
import { request } from 'umi';
import { Button,message  } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import ProForm,{ ModalForm, ProFormText } from '@ant-design/pro-form';
import { useRef, useState } from 'react';

const Role = ()=>{

  const actionRef = useRef()

  // eslint-disable-next-line react-hooks/rules-of-hooks
  const formRef = useRef();
  // eslint-disable-next-line react-hooks/rules-of-hooks
  const [modalVisit, setModalVisit] = useState(false);
  // eslint-disable-next-line react-hooks/rules-of-hooks
  const [formData, setFormData] = useState({});

  // eslint-disable-next-line react-hooks/rules-of-hooks
  const [modalFormTitle,setModalFormTitle] = useState("新建用户")

  // eslint-disable-next-line react-hooks/rules-of-hooks
  const [isModalVisible, setIsModalVisible] = useState(false);

  const column = [
    {
      title: '名称',
      dataIndex: 'name',
    },
    {
      title: '编码',
      dataIndex: 'code',
    },
    {
      title: '数据权限',
      dataIndex: 'dataAuthority',
      search:false
    },
    {
      title: '描述',
      dataIndex: 'remark',
      search:false
    },
    {
      title: '操作',
      valueType: 'option',
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      render: (text, record, _, action) => [
        <a
          key="editable"
          onClick={() => {
            setModalFormTitle("编辑角色")
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
              await request("/api/sys/role", {
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
    <>
      <ProTable
        actionRef={actionRef}
        columns={column}
        request={
          // eslint-disable-next-line @typescript-eslint/no-unused-vars
          async (params, sort, filter) => {
            const result = await request("/api/sys/role", {
              method: "GET",
              params: { param: JSON.stringify(params) }
            })
            return {
              data: result,
              success: true,
            }
          }
        }
        toolBarRender={() => [
          <ModalForm
            formRef={formRef}
          title={modalFormTitle}
          visible={modalVisit}
          onVisibleChange={setModalVisit}
          trigger={
          <Button type="primary" onClick={
            ()=>{
              setModalFormTitle("新建角色")
              setModalVisit(true)
              setFormData({})
              formRef.current.setFieldsValue({
                name:"",
                code:"",
                remark:"",
                dataAuthority:""
              })
            }
          }>
          <PlusOutlined />
            新建
          </Button>
        }
          modalProps={{
          onCancel: () => console.log('run'),
        }}
          onFinish={async (values) => {
            // eslint-disable-next-line no-param-reassign
            values.id = formData.id
            await request("/api/sys/role",{
              method:"POST",
              data:{param:JSON.stringify(values)},
              requestType: 'form'
            })
            message.success('提交成功');
            actionRef.current.reload()
            formRef.current.setFieldsValue({
              name:"",
              code:"",
              remark:"",
              dataAuthority:""
            })
            return true;
        }}
          >
          <ProForm.Group>
          <ProFormText width="md" name="name" label="名称" />
          <ProFormText width="md" name="code" label="编码" />
          </ProForm.Group>
          <ProForm.Group>
            <ProFormText width="md" name="dataAuthority" label="数据权限" />
            <ProFormText width="md" name="remark" label="描述" />
          </ProForm.Group>
          </ModalForm>
        ]}
      >

      </ProTable>
    </>
  )
}
export default Role
