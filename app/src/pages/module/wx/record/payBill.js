import ProTable from '@ant-design/pro-table';
import { request } from 'umi';
import { useRef } from 'react';
import {Button} from "antd";
import {PlusOutlined} from "@ant-design/icons";
import ProForm, {ModalForm, ProFormDatePicker} from "@ant-design/pro-form";

const url = '/api/wx/pay/bill'

async function list(params, options) {
  return  request(url, {
    method: 'GET',
    params: { ...params },
    ...(options || {}),
  });
}

async function bill(params, options) {
  return request("/api/wx/pay/bill/downloadbill", {
    method: 'GET',
    params: { ...params },
    ...(options || {}),
  });
}

const PayBill = ()=>{

  const actionRef = useRef();


  const columns=[
    {
      title:'交易时间',
      dataIndex:'tradeDate',
      valueType: 'dateTime',
      sorter: (a, b) => a.tradeDate - b.tradeDate,
    },
    {
      title:'微信订单号',
      dataIndex:'transactionId'
    },
    {
      title:'商品名称',
      dataIndex:'body'
    },
    {
      title:'总金额',
      dataIndex:'totalFee'
    },
    {
      title:'交易类型',
      dataIndex:'tradeType'
    },
    {
      title:'交易状态',
      dataIndex:'tradeState'
    },
    {
      title: '操作',
      valueType: 'option',
      render: (text, record) => [
        <a
          key="editable"
          onClick={() => {
          }}
        >
          查看
        </a>,
      ],
    },
  ]

  return(
    <>
      <ProTable
        actionRef={actionRef}
        columns={columns}
        request={list}
        toolBarRender={() => [
          <ModalForm
            key="form"
            title="下载账单"
            trigger={
              <Button key="button" icon={<PlusOutlined />} type="primary">
                下载账单
              </Button>
            }
            modalProps={{
              onCancel: () => console.log('run'),
            }}
            onFinish={async (values) => {
              console.log(values)
              await bill(values)
              if (actionRef.current) {
                actionRef.current.reload();
              }
              return true;
            }}
          >

            <ProForm.Group>
              <ProFormDatePicker name="billDate" label="账单日期" />
            </ProForm.Group>
          </ModalForm>
        ]}
      />
    </>
  )
}
export default PayBill
