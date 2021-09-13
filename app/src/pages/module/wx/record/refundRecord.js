import ProTable from '@ant-design/pro-table';
import { request } from 'umi';
import { useRef } from 'react';
import {Button} from "antd";
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

const RefundRecord = ()=>{

  const actionRef = useRef();


  const columns=[
    {
      title:'退款时间',
      dataIndex:'refundSuccessTime',
      valueType: 'date',
    },
    {
      title:'微信退款单号',
      dataIndex:'refundId'
    },
    {
      title:'商户退款单号',
      dataIndex:'outRefundNo'
    },
    {
      title:'退款金额',
      dataIndex:'refundFee'
    },
    {
      title:'退款状态',
      dataIndex:'refundStatus'
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
export default RefundRecord
