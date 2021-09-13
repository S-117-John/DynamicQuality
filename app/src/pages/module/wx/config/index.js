import ProTable from '@ant-design/pro-table';
import { ModalForm, ProFormText } from '@ant-design/pro-form';
import { Button, message } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import { request } from '@@/plugin-request/request';
import { useRef, useState } from 'react';

const WxConfig = ()=>{

  const actionRef = useRef();
  const formRef = useRef();
  const [modalVisit, setModalVisit] = useState(false);
  const [formData, setFormData] = useState({});
  const [modalFormTitle,setModalFormTitle] = useState("新建微信配置信息")


  const columns=[
    {
      title:'appId',
      dataIndex:'appId'
    },
    {
      title:'appSecret',
      dataIndex:'appSecret'
    },
    {
      title:'token',
      dataIndex:'token'
    },
    {
      title: '操作',
      valueType: 'option',
      render: (text, record) => [
        <a
          key="editable"
          onClick={() => {
            setModalFormTitle("编辑微信配置信息")
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
              await request("/api/wx/config", {
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
        search={false}
        columns={columns}
        request={
          async () => {
            const result = await request("/api/wx/config", {
              method: "GET",
            });
            return {
              data: result,
              success: true,
            };
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
              await request("/api/wx/config", {
                method: "POST",
                data: { "param": JSON.stringify(values) },
                requestType: 'form'
              });
              message.success('提交成功');
              actionRef.current.reload()
              return true;
            }}
          >
            <ProFormText width="md" name="appID" label="appID" />
            <ProFormText width="md" name="appSecret" label="appSecret"/>
            <ProFormText width="md" name="token" label="token" />

          </ModalForm>
        ]}
      />
    </>
  )
}
export default WxConfig
