import ProTable from '@ant-design/pro-table';
import { request } from 'umi';
import { useRef } from 'react';
import {Button} from "antd";
import {PlusOutlined} from "@ant-design/icons";
import ProForm, {ModalForm, ProFormDatePicker} from "@ant-design/pro-form";

const url = '/api/alipay/bill'

async function list(params, options) {
  return  request(url, {
    method: 'GET',
    params: { ...params },
    ...(options || {}),
  });
}

async function billDay(params, options) {
  return request("/api/alipay/bill/day", {
    method: 'GET',
    params: { ...params },
    ...(options || {}),
  });
}

const AliPayBill = ()=>{

  const actionRef = useRef();


  const columns=[

    {
      title:'支付宝交易号',
      dataIndex:'tradeNo'
    },
    {
      title:'商户订单号',
      dataIndex:'outTradeNo'
    },
    {
      title:'业务类型',
      dataIndex:'bizType',
      search: false
    },
    {
      title:'商品名称',
      dataIndex:'subject',
      search: false
    },
    {
      title:'创建时间',
      dataIndex:'startTime',
      valueType: 'dateTime',
      sorter: (a, b) => a.tradeDate - b.tradeDate,
      search: false
    },
    {
      title:'完成时间',
      dataIndex:'endTime',
      valueType: 'dateTime',
      sorter: (a, b) => a.tradeDate - b.tradeDate,
      search: false
    },
    {
      title:'对方账户',
      dataIndex:'buyerLogonId',
      search: false
    },
    {
      title:'订单金额',
      dataIndex:'totalAmount',
      search: false
    },
    {
      title:'商家实收',
      dataIndex:'receiptAmount',
      search: false
    },
    {
      title:'退款批次号',
      dataIndex:'refundBatchNo',
      search: false
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
              await billDay(values)
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
export default AliPayBill
