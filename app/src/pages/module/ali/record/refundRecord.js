import ProTable from '@ant-design/pro-table';
import { request } from 'umi';
import { useRef } from 'react';

const url = '/api/alipay/refund/record'

async function list(params, options) {
  return request(url, {
    method: 'GET',
    params: { ...params },
    ...(options || {}),
  });
}


const RefundRecord = ()=>{

  const actionRef = useRef();


  const columns=[
    {
      title:'退款时间',
      dataIndex:'gmtRefundPay',
      valueType: 'dateTime',
    },
    {
      title:'支付宝订单号',
      dataIndex:'tradeNo'
    },
    {
      title:'商户订单号',
      dataIndex:'outTradeNo',
      search: false
    },
    {
      title:'退款金额',
      dataIndex:'refundFee',
      search: false
    },
    {
      title:'退款状态',
      dataIndex:'tradeStatus',
      search: false
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
      />
    </>
  )
}
export default RefundRecord
