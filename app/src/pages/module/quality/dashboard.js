import React, {useEffect, useState} from 'react';
import { Statistic } from 'antd';
import ProCard from '@ant-design/pro-card';
import RcResizeObserver from 'rc-resize-observer';
import { Column } from '@ant-design/charts';

const Dashboard = ()=>{

  const { Divider } = ProCard;

  const [responsive, setResponsive] = useState(false);

  const [data, setData] = useState([]);

  const asyncFetch = () => {
    fetch('/api/quality/chart/index')
      .then((response) => response.json())
      .then((json) => setData(json))
      .catch((error) => {
        console.log('fetch data failed', error);
      });
  };

  useEffect(() => {
    asyncFetch();
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
            <Statistic title="今日上报单病种数量" value={79} />
          </ProCard>
          <Divider type={responsive ? 'horizontal' : 'vertical'} />
          <ProCard>
            <Statistic title="昨日上报单病种数量" value={1128}/>
          </ProCard>
          <Divider type={responsive ? 'horizontal' : 'vertical'} />
          <ProCard>
            <Statistic title="本月上报单病种数量" value={93} />
          </ProCard>
          <Divider type={responsive ? 'horizontal' : 'vertical'} />
          <ProCard>
            <Statistic title="累计审核成功数量" value={11289} />
          </ProCard>
        </ProCard.Group>
      </RcResizeObserver>
      <ProCard title="各病种上报数量" style={{ marginTop: 8 }}>
        <Column {...config} />

      </ProCard>

    </>
  )
}

export default Dashboard
