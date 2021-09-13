import ProTable from '@ant-design/pro-table';
import { request } from 'umi';
import { useRef } from 'react';

const url = '/api/alipay/trade/record'

async function list(params, options) {
  return request(url, {
    method: 'GET',
    params: { ...params },
    ...(options || {}),
  });
}


const TradeRecord = ()=>{

  const actionRef = useRef();


  const columns=[
    {
      title:'支付时间',
      dataIndex:'sendPayDate',
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
      title:'支付金额',
      dataIndex:'totalAmount',
      search: false
    },
    {
      title:'交易状态',
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
export default TradeRecord
