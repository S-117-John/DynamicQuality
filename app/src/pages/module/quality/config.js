import { request } from '@@/plugin-request/request';
import ProTable from '@ant-design/pro-table';
import { useRef, useState } from 'react';
import { history } from '@@/core/history';
import ProForm, { ModalForm, ProFormDigit, ProFormSelect, ProFormText } from '@ant-design/pro-form';
import { Button, message } from 'antd';
import { PlusOutlined } from '@ant-design/icons';

const Config = ()=>{

  const actionRef = useRef()

  const formRef = useRef();

  const [modalVisit, setModalVisit] = useState(false);

  const [formData, setFormData] = useState({});

  const [modalFormTitle,setModalFormTitle] = useState("新建单病种信息")

  const columns = [

    {
      title:'表单编码',
      dataIndex:'code'
    },
    {
      title:'单病种名称',
      dataIndex:'name'
    },
    {
      title:'单病种分类',
      dataIndex:'type',
      valueType:'select',
      valueEnum : {
        1: {
          text: '呼吸系统疾病/手术',
        },
        2: {
          text: '口腔系统疾病/手术',
        },
        3: {
          text: '泌尿系统疾病/操作',
        },
        4: {
          text: '神经系统疾病/手术',
        },
        5: {
          text: '生殖系统疾病/手术',
        },
        6: {
          text: '心血管系统疾病/手术',
        },
        7: {
          text: '眼科系统疾病/手术',
        },
        8: {
          text: '运动系统疾病/手术',
        },
        9: {
          text: '肿瘤(手术治疗)',
        },
        10: {
          text: '其他疾病/手术',
        },
      }
    },
    {
      title:'上报接口',
      dataIndex:'interfaceUrl'
    },
    {
      title:'患者分类接口',
      dataIndex:'remarks'
    },
    {
      title:'患者信息接口',
      dataIndex:'icd10'
    },
    {
      title: '操作',
      valueType: 'option',
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      render: (text, record, _, action) => [
        <a
          key="editable"
          onClick={() => {
            setModalFormTitle("编辑病种信息")
            setFormData(record)
            formRef.current.setFieldsValue(record)
            setModalVisit(true)
          }}
        >
          编辑
        </a>,
        <a
          onClick={async () => {
            const param = {
              id: record.id,
            }
            await request("/api/quality/view", {
              method: "DELETE",
              params: { param: JSON.stringify(param) },
            });
            actionRef.current.reload()
          }}
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
        columns={columns}
        rowKey="id"
        request={
          async (params, sort, filter,) => {
            const result = await request("/api/quality/config", {
              method: "GET",
              params: { "param": JSON.stringify(params) },
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
                  formRef.current.setFieldsValue({
                    dictLabel:"",
                    dictValue:"",
                    treeSort:0
                  })
                }
              }>
                <PlusOutlined />
                新建
              </Button>
            }
            onFinish={async (values) => {
              // eslint-disable-next-line no-param-reassign
              // eslint-disable-next-line no-param-reassign
              values.id = formData.id
              await request("/api/quality/config", {
                method: "POST",
                data: { "param": JSON.stringify(values) },
                requestType: 'form'
              });
              message.success('提交成功');
              actionRef.current.reload()
              return true;
            }}
          >
            <ProForm.Group>
              <ProFormText width="md" name="code" label="病种编码" />
              <ProFormText width="md" name="name" label="病种名称"  />
            </ProForm.Group>
            <ProForm.Group>
              <ProFormText width="md" name="remarks" label="患者分类接口"  />
              <ProFormText width="md" name="interfaceUrl" label="上报接口" />
            </ProForm.Group>
            <ProForm.Group>
              <ProFormSelect
                width="md"
                name="type"
                label="病种分类"
                valueEnum={{
                  1: {
                    text: '呼吸系统疾病/手术',
                  },
                  2: {
                    text: '口腔系统疾病/手术',
                  },
                  3: {
                    text: '泌尿系统疾病/操作',
                  },
                  4: {
                    text: '神经系统疾病/手术',
                  },
                  5: {
                    text: '生殖系统疾病/手术',
                  },
                  6: {
                    text: '心血管系统疾病/手术',
                  },
                  7: {
                    text: '眼科系统疾病/手术',
                  },
                  8: {
                    text: '运动系统疾病/手术',
                  },
                  9: {
                    text: '肿瘤(手术治疗)',
                  },
                  10: {
                    text: '其他疾病/手术',
                  },
                }}
              />
              <ProFormText width="md" name="icd10" label="患者信息接口" />
            </ProForm.Group>

          </ModalForm>
        ]}
      />
    </>
  )
}
export default Config
