import React, {useEffect, useState} from 'react';
import { Statistic } from 'antd';
import ProCard from '@ant-design/pro-card';
import RcResizeObserver from 'rc-resize-observer';
import { Column } from '@ant-design/charts';
import {request} from "umi";

const Dashboard = ()=>{

  const { Divider } = ProCard;

  const [responsive, setResponsive] = useState(false);

  const [alipayData,setAlipayData] = useState({"trade":0,"refund":0});

  const [data,setData] = useState([]);



  async function list(params, options) {
    const result = await request('/api/alipay/data', {
      method: 'GET',
      params: { ...params },
      ...(options || {}),
    });
    setAlipayData(result)
    setData(result.total)
  }

  useEffect(() => {

    list();

  }, []);

  const config = {
    data: data,
    xField: 'name',
    yField: 'count',
    xAxis: { label: { autoRotate: false } },
    scrollbar: { type: 'horizontal' },
  };

  return(
    <>
      <RcResizeObserver
        key="resize-observer"
        onResize={(offset) => {
          setResponsive(offset.width < 596);
        }}
      >
        <ProCard.Group title="" direction={responsive ? 'column' : 'row'}>
          <ProCard>
            <Statistic title="今日支付宝交易笔数" value={alipayData.trade} />
          </ProCard>
          <Divider type={responsive ? 'horizontal' : 'vertical'} />
          <ProCard>
            <Statistic title="今日微信交易笔数" value={0}/>
          </ProCard>
          <Divider type={responsive ? 'horizontal' : 'vertical'} />
          <ProCard>
            <Statistic title="今日支付宝退款笔数" value={alipayData.refund} />
          </ProCard>
          <Divider type={responsive ? 'horizontal' : 'vertical'} />
          <ProCard>
            <Statistic title="今日微信退款笔数" value={0} />
          </ProCard>
        </ProCard.Group>
      </RcResizeObserver>

      <ProCard split="vertical" style={{ marginTop: 8 }}>
        <ProCard title="支付宝（总收入/退款）">
          <Column {...config} />
        </ProCard>
        <ProCard title="微信（总收入/退款）">
          <Column {...config} />
        </ProCard>
      </ProCard>

    </>
  )
}

export default Dashboard
