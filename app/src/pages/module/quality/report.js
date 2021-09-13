import ProCard from '@ant-design/pro-card';
import ProList from '@ant-design/pro-list';
import { request } from '@@/plugin-request/request';
import { useState } from 'react';
import { Button, Image } from 'antd';
import { SearchOutlined } from '@ant-design/icons';
import { history } from '@@/core/history';

const Report = ()=>{

  const dataSource = [
    {
      name: '呼吸系统疾病/手术',
      image: '',
      desc: '',
      type: '1'
    },
    {
      name: '口腔系统疾病/手术',
      image: '',
      desc: '',
      type: '2'
    },
    {
      name: '泌尿系统疾病/操作',
      image: '',
      desc: '',
      type: '3'
    },
    {
      name: '神经系统疾病/手术',
      image: '',
      desc: '',
      type: '4'
    },
    {
      name: '生殖系统疾病/手术',
      image: '',
      desc: '',
      type: '5'
    },
    {
      name: '心血管系统疾病/手术',
      image: '',
      desc: '',
      type: '6'
    },
    {
      name: '眼科系统疾病/手术',
      image: '',
      desc: '',
      type: '7'
    },
    {
      name: '运动系统疾病/手术',
      image: '',
      desc: '',
      type: '8'
    },
    {
      name: '肿瘤(手术治疗)',
      image: '',
      desc: '',
      type: '9'
    },
    {
      name: '其他疾病/手术',
      image: '',
      desc: '',
      type: '10'
    },
  ];

  const [diseaseList,setDiseaseList] = useState([])

  const [title,setTitle] = useState("")


  return(
    <>
      <ProCard split="vertical">
        <ProCard  colSpan={6}>
          <ProList
            dataSource={dataSource}
            split={true}
            onRow={(record) => {
              return {
                onMouseEnter: () => {
                  console.log(record);
                },
                onClick: async () => {
                  setTitle(record.name)
                  const result = await request("/api/quality/config", {
                    method: "GET",
                    params: { "param": JSON.stringify(record) },
                  });
                  setDiseaseList(result)
                },
              };
            }}
            metas={{
              title: {
                dataIndex: 'name',
              },
              avatar: {
                dataIndex: 'image',
              },
              description: {
                dataIndex: 'desc',
              },
            }}
          />
        </ProCard>
        <ProCard title={title} headerBordered colSpan={18}>
          <ProCard style={{ marginTop: 8 }} gutter={[16, 16]} wrap>
            {
              // eslint-disable-next-line @typescript-eslint/no-unused-vars
              diseaseList.map((value, index, array)=>{
                return(
                  <ProCard
                    colSpan={{ xs: 24, sm: 6, md: 6, lg: 6, xl: 6 }}
                    layout="center"
                    bordered
                    title={value.name}
                    extra={
                      <Button
                        type="primary"
                        shape="circle"
                        icon={<SearchOutlined />}
                        onClick={
                          ()=>{
                            history.push({
                              pathname: 'patient',
                              query: {
                                formCode: value.code,
                              },
                            });
                          }
                        }
                      />
                    }
                    direction="column"
                  >
                    <Image

                      src="data:image/.png;base64,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"
                    />
                  </ProCard>
                )
              })
            }
          </ProCard>
        </ProCard>
      </ProCard>
    </>
  )
}
export default Report
