import { PageContainer } from '@ant-design/pro-layout';
import ProTable from '@ant-design/pro-table';
import { request } from '@@/plugin-request/request';
import { PlusOutlined } from '@ant-design/icons';
import { Button, message, Modal, Transfer } from 'antd';
import ProForm, { ModalForm, ProFormSelect, ProFormText } from '@ant-design/pro-form';
import { useRef, useState } from 'react';



const getDataList = async () => {
  const result = await request("/api/sys/user", {});
  return {
    data: result,
    // success 请返回 true，
    // 不然 table 会停止解析数据，即使有数据
    success: true,
    // 不传会使用 data 的长度，如果是分页一定要传
    // total: number,
  };
}

const save = (values) => {
  request("/api/sys/user", {
    method: "POST",
    data: { "param": JSON.stringify(values) },
    requestType: 'form'
  });
}

const userList = ()=>{
  // eslint-disable-next-line react-hooks/rules-of-hooks
  const actionRef = useRef();
  // eslint-disable-next-line react-hooks/rules-of-hooks
  const formRef = useRef();
  // eslint-disable-next-line react-hooks/rules-of-hooks
  const [modalVisit, setModalVisit] = useState(false);
  // eslint-disable-next-line react-hooks/rules-of-hooks
  const [formData, setFormData] = useState({});

  // eslint-disable-next-line react-hooks/rules-of-hooks
  const [modalFormTitle,setModalFormTitle] = useState("新建用户")

  // eslint-disable-next-line react-hooks/rules-of-hooks
  const [isModalVisible, setIsModalVisible] = useState(false);

  const [roleList,setRoleList] = useState([])

  const [targetKeys, setTargetKeys] = useState(roleList);

  const [selectedKeys, setSelectedKeys] = useState([]);

  const [currentUser,setCurrentUser] = useState({})

  const columns = [

    {
      title:'姓名',
      dataIndex:'name'
    },
    {
      title:'账号',
      dataIndex:'username'
    },
    {
      title:'机构',
      dataIndex:'deptName',
      search: false
    },
    {
      title: '操作',
      valueType: 'option',
      render: (text, record) => [
        <a
          key="editable"
          onClick={() => {
            setModalFormTitle("编辑用户")
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
              await request("/api/sys/user", {
                method: "DELETE",
                params: { "param": JSON.stringify(record) },
              });
              actionRef.current.reload()
            }
          }
        >
          删除
        </a>,
        <a
          key="role"
          onClick={
            async () => {
              // eslint-disable-next-line @typescript-eslint/no-shadow
              const roleList = await request("/api/sys/role", {
                method: "GET"
              })
              const userRoleList = await request("/api/sys/user/role", {
                method: "GET",
                params:{param:JSON.stringify(record)}
              })
              setRoleList(roleList)
              setTargetKeys(userRoleList)
              setCurrentUser(record)
              setIsModalVisible(true)
            }
          }
        >
          角色分配
        </a>,
      ],
    },

  ]

  return(
    <div>
      <PageContainer>
        <ProTable
          actionRef={actionRef}
          columns={columns}
          request={getDataList}
          toolBarRender={() => [
            <ModalForm
              formRef={formRef}
              title={modalFormTitle}
              visible={modalVisit}
              onVisibleChange={setModalVisit}
              trigger={
                <Button type="primary" onClick={
                  ()=>{
                    setModalFormTitle("新建用户")
                    setModalVisit(true)
                    setFormData({})
                    formRef.current.setFieldsValue({
                      name:"",
                      password:"",
                      username:"",
                      deptId:""
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
                await save(values);
                actionRef.current.reload()
                return true;
              }}
            >
              <ProForm.Group>
                <ProFormText width="md" name="username" label="账户" placeholder="请输入名称" />
                <ProFormText width="md" name="password" label="密码" placeholder="请输入编码" />
              </ProForm.Group>
              <ProForm.Group>
                <ProFormText width="md" name="name" label="姓名" placeholder="请输入编码" />
                <ProFormSelect
                  width="md"
                  name="deptId"
                  label="部门"
                  showSearch
                  params={{}}
                  request={
                    async () => {
                      const result = await request("/api/sys/dept/select", {
                        method: "GET",
                      });
                      return result
                    }
                  }
                  placeholder="选择部门"
                />
              </ProForm.Group>

            </ModalForm>
          ]}
        />
      </PageContainer>

      <Modal title="角色分配"
             visible={isModalVisible}
             onOk={
               async () => {

                 setIsModalVisible(false)
                 console.log(targetKeys)
                 await request("/api/sys/user/role", {
                   method:"POST",
                   params:{param:JSON.stringify({user:currentUser,roleIds:targetKeys})}
                 })
                 setTargetKeys([])
                 setSelectedKeys([])
               }
             }
             onCancel={
               ()=>{
                 setIsModalVisible(false)
               }
             }>

        <Transfer
          dataSource={roleList}
          titles={['Source', 'Target']}
          targetKeys={targetKeys}
          selectedKeys={selectedKeys}
          render={item => item.name}
          onChange={
            (nextTargetKeys, direction, moveKeys)=>{
              setTargetKeys(nextTargetKeys);
            }
          }
          onSelectChange={
            (sourceSelectedKeys, targetSelectedKeys)=>{
              setSelectedKeys([...sourceSelectedKeys, ...targetSelectedKeys]);

            }
          }

        />
      </Modal>

    </div>
  )
}

export default userList
