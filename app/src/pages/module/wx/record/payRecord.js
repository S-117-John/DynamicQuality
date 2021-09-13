import ProTable from '@ant-design/pro-table';
import { request } from 'umi';
import { useRef } from 'react';
import {Button, Tag} from "antd";
import {PlusOutlined} from "@ant-design/icons";
import ProForm, {ModalForm, ProFormDatePicker} from "@ant-design/pro-form";

const url = '/api/wx/trade/record'

async function list(params, options) {
  return request(url, {
    method: 'GET',
    params: { ...params },
    ...(options || {}),
  });
}

async function bill(params, options) {
  return request("/api/wx/trade/record/bill", {
    method: 'GET',
    params: { ...params },
    ...(options || {}),
  });
}

const PayRecord = ()=>{

  const actionRef = useRef();


  const columns=[
    {
      title:'支付时间',
      dataIndex:'timeEnd',
      valueType: 'dateTime',
      search: false
    },
    {
      title:'支付时间',
      dataIndex:'timeEnd',
      valueType: 'date',
      hideInTable: true
    },
    {
      title:'微信订单号',
      dataIndex:'transactionId'
    },
    {
      title:'商户订单号',
      dataIndex:'outTradeNo'
    },
    {
      title:'支付金额',
      dataIndex:'totalFee',
      search:false,
      render:(text)=>[

        <a>{text/100}</a>,

      ]
    },
    {
      title:'交易状态',
      dataIndex:'tradeState',
      search:false,
      render:(text) =>[
        <Tag color="geekblue">{text}</Tag>
      ]
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
          // <ModalForm
          //   key="form"
          //   title="新建表单"
          //   trigger={
          //     <Button key="button" icon={<PlusOutlined />} type="primary">
          //       下载账单
          //     </Button>
          //   }
          //   modalProps={{
          //     onCancel: () => console.log('run'),
          //   }}
          //   onFinish={async (values) => {
          //     console.log(values)
          //     await bill(values)
          //     if (actionRef.current) {
          //       actionRef.current.reload();
          //     }
          //     return true;
          //   }}
          // >
          //
          //   <ProForm.Group>
          //     <ProFormDatePicker name="billDate" label="账单日期" />
          //   </ProForm.Group>
          // </ModalForm>
        ]}
      />
    </>
  )
}
export default PayRecord
