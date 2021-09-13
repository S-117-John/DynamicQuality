import { useRef, useState } from 'react';
import { request } from '@@/plugin-request/request';
import { PageContainer } from '@ant-design/pro-layout';
import ProTable from '@ant-design/pro-table';
import ProForm, { ModalForm, ProFormDigit, ProFormText } from '@ant-design/pro-form';
import { Button, message } from 'antd';
import { PlusOutlined } from '@ant-design/icons';

const DictData = (props)=>{


  // eslint-disable-next-line react-hooks/rules-of-hooks
  const actionRef = useRef();
  // eslint-disable-next-line react-hooks/rules-of-hooks
  const formRef = useRef();
  const [modalVisit, setModalVisit] = useState(false);
  const [formData, setFormData] = useState({});
  const [modalFormTitle,setModalFormTitle] = useState("新建字典数据")

  const columns = [

    {
      title:'字典标签',
      dataIndex:'dictLabel',

    },
    {
      title:'字典值',
      dataIndex:'dictValue',

    },
    {
      title:'排序',
      dataIndex:'treeSort'
    },
    {
      title:'字典类型',
      dataIndex:'dictType',

    },

    {
      title: '操作',
      valueType: 'option',
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      render: (text, record, _, action) => [
        <a
          key="editable"
          onClick={() => {
            setModalFormTitle("编辑字典数据")
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
              await request("/api/sys/dict/data", {
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
          search={false}
          actionRef={actionRef}
          columns={columns}
          request={
            // eslint-disable-next-line @typescript-eslint/no-unused-vars
            async (params, sort, filter) => {
              // eslint-disable-next-line no-param-reassign
              params.dictType = props.location.query.dictType
              const result = await request("/api/sys/dict/data", {
                params: {param:JSON.stringify(params)}
              });
              return {
                data: result,
                success: true,
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
                values.dictType = props.location.query.dictType
                // eslint-disable-next-line no-param-reassign
                values.id = formData.id
                await request("/api/sys/dict/data", {
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
                <ProFormText width="md" name="dictLabel" label="字典标签" placeholder="请输入字典标签" />
                <ProFormText width="md" name="dictValue" label="字典值" placeholder="请输入字典标签" />
                <ProFormDigit  width="md" name="treeSort" label="排序" placeholder="请输入排序" />
              </ProForm.Group>

            </ModalForm>
          ]}
        />
      </PageContainer>

    </div>
  )

}

export default DictData
