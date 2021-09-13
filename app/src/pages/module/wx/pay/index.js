import ProTable from '@ant-design/pro-table';
import { ModalForm, ProFormText } from '@ant-design/pro-form';
import { Button, message } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import { request } from '@@/plugin-request/request';
import { useRef, useState } from 'react';

const WxPayConfig = ()=>{

  const title = '微信支付配置'
  const url = '/api/wx/pay/config'
  const formValueInit = {
    appId:"",
    appName:"",
    certificatePath:"",
    mchId:"",
    paySignKey:"",
  }
  const formItem = (
      <>
        <ProFormText width="md" name="appId" label="appID" />
        <ProFormText width="md" name="appName" label="应用名称"/>
        <ProFormText width="md" name="certificatePath" label="证书路径" />
        <ProFormText width="md" name="mchId" label="商户ID" />
        <ProFormText width="md" name="paySignKey" label="支付密钥" />
      </>
    )


  const actionRef = useRef();
  const formRef = useRef();
  const [modalVisit, setModalVisit] = useState(false);
  const [formData, setFormData] = useState({});
  const [modalFormTitle,setModalFormTitle] = useState(`新建${title}信息`)


  const columns=[
    {
      title:'appId',
      dataIndex:'appId'
    },
    {
      title:'应用名称',
      dataIndex:'appName'
    },
    {
      title:'证书路径',
      dataIndex:'certificatePath'
    },
    {
      title:'商户ID',
      dataIndex:'mchId'
    },
    {
      title:'支付密钥',
      dataIndex:'paySignKey'
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
