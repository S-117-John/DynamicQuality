import ProForm, {
  ProFormList,
  ProFormText,
  ProFormGroup,
  ProFormDependency,
  ProFormSelect, ProFormSwitch,
} from '@ant-design/pro-form';
import { EditableProTable } from '@ant-design/pro-table';
import React, { useRef, useState } from 'react';
import { request } from '@@/plugin-request/request';
import Form from 'antd/es/form/Form';
import { Button } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import ProCard from '@ant-design/pro-card';





const CreateForm = (props)=>{

  const [rules, setRules] = useState([]);
  const [editableKeys, setEditableRowKeys] = useState([]);
  const [dataSource, setDataSource] = useState([]);
  const actionRef = useRef();
  const rulesRef = useRef();
  const [form] = Form.useForm();
  const formRef = useRef();
  const [currentColumn,setCurrentColumn] = useState({})

  const rulesColumn = [
    {
      title: '当前值',
      dataIndex: 'value',
    },
    {
      title: '目标列',
      dataIndex: 'target',

    },
    {
      title: '操作',
      valueType: 'option',
      render: (text, record, _, action) => [
        <a
          key="editable"
          onClick={() => {
            action?.startEditable?.(record.id);
          }}
        >
          编辑
        </a>,
        <a
          key="delete"
          onClick={() => {
            setDataSource(dataSource.filter((item) => item.id !== record.id));
          }}
        >
          删除
        </a>,
      ],
    },
  ]

  const columns = [
    {
      title: '名称',
      dataIndex: 'name',
      width: 150,
    },
    {
      title: '编码',
      dataIndex: 'code',
      width: 150,

    },
    {
      title: '表单类型',
      dataIndex: 'type',
      valueType: 'select',
      width: 150,
      valueEnum:{
        text: {
          text: '文本',

        },
        select: {
          text: '下拉',

        },
        radio: {
          text: '单选',
        },
        checkbox: {
          text: '复选',
        },
        date:{
          text: '日期',
        },
        dateTime:{
          text: '时间日期',
        },
        digit:{
          text: '数字',
        },
        jsonCode:{
          text: '代码框',
        }
      },
    },
    {
      title: '验证规则',
      dataIndex: 'rules',
      valueType: 'jsonCode',
    },
    {
      title: '必填',
      dataIndex: 'required',
      valueType: 'checkbox',
      valueEnum: {
        y: "是",
      },
      width: 50,
    },
    {
      title: '默认值',
      dataIndex: 'initData',
      width: 100,

    },
    {
      title: '排序',
      dataIndex: 'sort',
      width: 100,
    },

    {
      title: '操作',
      valueType: 'option',
      width: 150,
      render: (text, record, index, action) => [
        <a
          key="editable"
          onClick={() => {
            // action?.startEditable?.(index);
            action.startEditable(index)
          }}
        >
          编辑
        </a>,
        <a
          key="editable"
          onClick={async () => {
            // action?.startEditable?.(index);
            setCurrentColumn(record)
            rulesRef.current.reload()
          }}
        >
          约束
        </a>,
        <a
          key="delete"
          onClick={async () => {
            const param = {
              id: record.id,
            }
            await request("/api/quality/form", {
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
    <div>
      <ProCard
        split="vertical"
        bordered
        headerBordered
        gutter={8}

      >
        <ProCard
          colSpan={18}

        >
          <ProForm
            form={form}
            formRef={formRef}
            submitter={{
              render: (props, doms) => {

                return [];
              },
            }}
          >
            <ProForm.Group>
              <ProFormText
                width="md"
                name="formName"
                label="表单名称"
                initialValue={props.location.query.formName}
                rules={
                  [
                    {
                      required: true,
                      message: '请填写表单名称',
                    },
                  ]
                }
              />
              <ProFormText
                width="md"
                name="formCode"
                label="表单编码"
                initialValue={props.location.query.formCode}
                rules={
                  [
                    {
                      required: true,
                      message: '请填写表单编码',
                    },
                  ]
                }
              />
            </ProForm.Group>
            <ProForm.Item
              label="表单数据"
              trigger="onValuesChange"
            >
              <EditableProTable
                scroll={{ y: 600 }}
                bordered
                actionRef={actionRef}
                columns={columns}
                toolBarRender={() => [
                  <Button key="button" icon={<PlusOutlined />} type="primary">
                    返回
                  </Button>,
                ]}
                request={async () => {
                  const code = formRef.current.getFieldValue('formCode')
                  const param = {
                    formCode: code,
                  }
                  const result = await request("/api/quality/form", {
                    method: "GET",
                    params: {param:JSON.stringify(param)},
                  });
                  return {
                    data: result,
                    success:true
                  }
                }}
                editable={{
                  type: 'multiple',
                  editableKeys,

                  onSave: async (rowKey, data, row) => {
                    try {
                      await form.validateFields();
                      const formName = formRef.current.getFieldValue('formName')
                      const formCode = formRef.current.getFieldValue('formCode')
                      // eslint-disable-next-line no-param-reassign
                      data.formName = formName
                      // eslint-disable-next-line no-param-reassign
                      data.formCode = formCode
                      await request("/api/quality/form", {
                        method: "POST",
                        data: { "param": JSON.stringify(data) },
                        requestType: 'form'
                      });
                      actionRef.current.reload()
                    } catch (errorInfo) {
                      actionRef.current.reload()
                    }
                  },
                  onChange: setEditableRowKeys,
                }}
              />
            </ProForm.Item>
          </ProForm>

        </ProCard>

        <ProCard
          colSpan={6}
          style={{
            height: '100vh',
            overflow: 'auto',
            boxShadow: '2px 0 6px rgba(0, 21, 41, 0.35)',
          }}
          title={currentColumn.code}
        >
          <ProForm
            onFinish={async (values) => {
              // eslint-disable-next-line no-param-reassign
              values.data = currentColumn
              await request("/api/quality/form/rules", {
                method: "POST",
                data: { "param": JSON.stringify(values) },
                requestType: 'form'
              });
              rulesRef.current.reload()
              actionRef.current.reload()
            }}
          >
            <ProForm.Group
            >
              <ProForm.Item
                label="约束条件"
                name="rules"
              >
                <EditableProTable
                  value={dataSource}
                  onChange={setDataSource}
                  columns = {rulesColumn}
                  actionRef={rulesRef}
                  request={async () => {
                    const formCodeVar = currentColumn.formCode
                    const codeVar = currentColumn.code
                    const param = {
                      formCode: formCodeVar,
                      code:codeVar
                    }
                    const result = await request("/api/quality/form/rules", {
                      method: "GET",
                      params: { param: JSON.stringify(param) },
                    });
                    return {
                      data: result,
                      success:true
                    }
                  }}
                />

              </ProForm.Item>
            </ProForm.Group>

          </ProForm>

        </ProCard>

      </ProCard>

    </div>
  )
}

export default CreateForm


