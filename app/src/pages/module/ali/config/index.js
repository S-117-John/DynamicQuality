import ProTable from '@ant-design/pro-table';
import { ModalForm, ProFormText } from '@ant-design/pro-form';
import { Button, message } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import { request } from '@@/plugin-request/request';
import { useRef, useState } from 'react';

const WxPayConfig = ()=>{

  const title = '支付宝支付配置'
  const url = '/api/ali/pay/config'
  const actionRef = useRef();
  const formRef = useRef();
  const [modalVisit, setModalVisit] = useState(false);
  const [formData, setFormData] = useState({});
  const [modalFormTitle,setModalFormTitle] = useState(`新建${title}信息`)
  const formValueInit = {
    appId:"",
    appName:"",
    gateway:"",
    payPublicKey:"",
    privateKey:"",
    publicKey:"",
  }
  const formItem = (
      <>
        <ProFormText width="md" name="appId" label="应用ID" />
        <ProFormText width="md" name="appName" label="应用名称"/>
        <ProFormText width="md" name="gateway" label="支付宝网关" />
        <ProFormText width="md" name="payPublicKey" label="支付宝公钥" />
        <ProFormText width="md" name="privateKey" label="应用私钥" />
        <ProFormText width="md" name="publicKey" label="应用公钥" />
      </>
    )

  const columns=[
    {
      title:'appId',
      dataIndex:'appId',

    },
    {
      title:'应用名称',
      dataIndex:'appName',

    },
    {
      title:'支付宝网关',
      dataIndex:'gateway',

    },
    {
      title:'支付宝公钥',
      dataIndex:'payPublicKey',
      ellipsis: true
    },
    {
      title:'应用私钥',
      dataIndex:'privateKey',
      ellipsis: true
    },
    {
      title:'应用公钥',
      dataIndex:'publicKey',
      ellipsis: true
    },
    {
      title: '操作',
      valueType: 'option',
      render: (text, record) => [
        <a
          key="editable"
          onClick={() => {
            setModalFormTitle(`编辑${title}信息`)
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
              await request(url, {
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
            const result = await request(url, {
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
                  formRef.current.setFieldsValue(formValueInit)
                }
              }>
                <PlusOutlined />
                新建
              </Button>
            }
            onFinish={async (values) => {
              // eslint-disable-next-line no-param-reassign
              values.id = formData.id
              await request(url, {
                method: "POST",
                data: { "param": JSON.stringify(values) },
                requestType: 'form'
              });
              message.success('提交成功');
              actionRef.current.reload()
              return true;
            }}
          >
            {formItem}

          </ModalForm>
        ]}
      />
    </>
  )
}
export default WxPayConfig
