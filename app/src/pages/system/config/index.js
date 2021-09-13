import ProTable from '@ant-design/pro-table';
import { PageContainer } from '@ant-design/pro-layout';
import { request } from '@@/plugin-request/request';
import { history } from '@@/core/history';
import { useRef, useState } from 'react';
import ProForm, { ModalForm, ProFormText } from '@ant-design/pro-form';
import { Button, message } from 'antd';
import { PlusOutlined } from '@ant-design/icons';




const ConfigList = ()=>{

  // eslint-disable-next-line react-hooks/rules-of-hooks
  const actionRef = useRef();
  // eslint-disable-next-line react-hooks/rules-of-hooks
  const formRef = useRef();
  const [modalVisit, setModalVisit] = useState(false);
  const [formData, setFormData] = useState({});
  const [modalFormTitle,setModalFormTitle] = useState("新建系统参数")

  const columns = [

    {
      title:'参数名称',
      dataIndex:'configName'
    },
    {
      title:'参数key',
      dataIndex:'configKey',
      search:false
    },
    {
      title:'参数值',
      dataIndex:'configValue',
      search:false
    },
    {
      title:'参数说明',
      dataIndex:'remarks',
      search:false,
      width:100
    },
    {
      title: '操作',
      valueType: 'option',
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      render: (text, record, _, action) => [
        <a
          key="editable"
          onClick={() => {
            setModalFormTitle("编辑字典类型")
            setFormData(record)
            formRef.current.setFieldsValue(record)
            setModalVisit(true)
          }}
        >
          编辑
        </a>,
        <a
          key="view"
          onClick={
            async () => {
              await request("/api/sys/config", {
                method: "DELETE",
                params: { "param": JSON.stringify(record) },
              });
              actionRef.current.reload()
            }
          }
        >
          删除
        </a>,
      ],
    },

  ]

  return(
    <div>
      <PageContainer>
        <ProTable
          bordered
          actionRef={actionRef}
          columns={columns}
          request={
            // eslint-disable-next-line @typescript-eslint/no-unused-vars
            async (params, sort, filter) => {
              const result = await request("/api/sys/config", {
                method:"GET",
                params:{param:JSON.stringify(params)}
              });
              return {
                data: result,
                // success 请返回 true，
                // 不然 table 会停止解析数据，即使有数据
                success: true,
                // 不传会使用 data 的长度，如果是分页一定要传
                // total: number,
              };
            }
          }
          toolBarRender={() => [
            // <Button key="button" icon={<PlusOutlined />} type="primary">
            //   新建
            // </Button>,
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
                      dictName:"",
                      dictType:""
                    })
                  }
                }>
                  <PlusOutlined />
                  新建
                </Button>
              }
              onFinish={async (values) => {
                // eslint-disable-next-line no-param-reassign
                values.id = formData.id
                await request("/api/sys/config", {
                  method: "POST",
                  data: { "param": JSON.stringify(values) },
                  requestType: 'form'
                });
                message.success('提交成功');
                actionRef.current.reload()
                return true;
              }}
            >
              <ProFormText width="md" name="configName" label="参数名称" placeholder="请输入名称" />
              <ProFormText width="md" name="configKey" label="参数key" placeholder="请输入编码" />
              <ProFormText width="md" name="configValue" label="参数值" placeholder="请输入编码" />
              <ProFormText width="md" name="remarks" label="参数说明" placeholder="请输入编码" />

            </ModalForm>
          ]}
        />
      </PageContainer>

    </div>
  )
}

export default ConfigList
