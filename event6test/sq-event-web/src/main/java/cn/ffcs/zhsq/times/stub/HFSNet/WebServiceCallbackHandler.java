
/**
 * WebServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package cn.ffcs.zhsq.times.stub.HFSNet;

    /**
     *  WebServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class WebServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public WebServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public WebServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for delFaceDB method
            * override this method for handling normal response from delFaceDB operation
            */
           public void receiveResultdelFaceDB(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.DelFaceDBRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from delFaceDB operation
           */
            public void receiveErrordelFaceDB(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for addFaceDB method
            * override this method for handling normal response from addFaceDB operation
            */
           public void receiveResultaddFaceDB(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.AddFaceDBRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addFaceDB operation
           */
            public void receiveErroraddFaceDB(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for fDProcImage method
            * override this method for handling normal response from fDProcImage operation
            */
           public void receiveResultfDProcImage(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.FDProcImageRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from fDProcImage operation
           */
            public void receiveErrorfDProcImage(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getWorkingStatus method
            * override this method for handling normal response from getWorkingStatus operation
            */
           public void receiveResultgetWorkingStatus(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetWorkingStatusRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getWorkingStatus operation
           */
            public void receiveErrorgetWorkingStatus(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for startDuplicateCheck method
            * override this method for handling normal response from startDuplicateCheck operation
            */
           public void receiveResultstartDuplicateCheck(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.StartDuplicateCheckRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from startDuplicateCheck operation
           */
            public void receiveErrorstartDuplicateCheck(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getAlarmList method
            * override this method for handling normal response from getAlarmList operation
            */
           public void receiveResultgetAlarmList(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetAlarmListRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getAlarmList operation
           */
            public void receiveErrorgetAlarmList(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for delTask method
            * override this method for handling normal response from delTask operation
            */
           public void receiveResultdelTask(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.DelTaskRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from delTask operation
           */
            public void receiveErrordelTask(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getStorageVolumes method
            * override this method for handling normal response from getStorageVolumes operation
            */
           public void receiveResultgetStorageVolumes(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetStorageVolumesRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getStorageVolumes operation
           */
            public void receiveErrorgetStorageVolumes(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for stopImportTask method
            * override this method for handling normal response from stopImportTask operation
            */
           public void receiveResultstopImportTask(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.StopImportTaskRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from stopImportTask operation
           */
            public void receiveErrorstopImportTask(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for factoryReset method
            * override this method for handling normal response from factoryReset operation
            */
           public void receiveResultfactoryReset(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.FactoryResetRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from factoryReset operation
           */
            public void receiveErrorfactoryReset(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getDuplicateCheckTask method
            * override this method for handling normal response from getDuplicateCheckTask operation
            */
           public void receiveResultgetDuplicateCheckTask(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetDuplicateCheckTaskRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getDuplicateCheckTask operation
           */
            public void receiveErrorgetDuplicateCheckTask(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for addGroupRecords method
            * override this method for handling normal response from addGroupRecords operation
            */
           public void receiveResultaddGroupRecords(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.AddGroupRecordsRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addGroupRecords operation
           */
            public void receiveErroraddGroupRecords(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getSnapRetrievalProgress method
            * override this method for handling normal response from getSnapRetrievalProgress operation
            */
           public void receiveResultgetSnapRetrievalProgress(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetSnapRetrievalProgressRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getSnapRetrievalProgress operation
           */
            public void receiveErrorgetSnapRetrievalProgress(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for addGroupRecord method
            * override this method for handling normal response from addGroupRecord operation
            */
           public void receiveResultaddGroupRecord(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.AddGroupRecordRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addGroupRecord operation
           */
            public void receiveErroraddGroupRecord(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getFaceDBs method
            * override this method for handling normal response from getFaceDBs operation
            */
           public void receiveResultgetFaceDBs(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetFaceDBsRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getFaceDBs operation
           */
            public void receiveErrorgetFaceDBs(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getDeviceLog method
            * override this method for handling normal response from getDeviceLog operation
            */
           public void receiveResultgetDeviceLog(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetDeviceLogRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getDeviceLog operation
           */
            public void receiveErrorgetDeviceLog(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getOneVXOriImag method
            * override this method for handling normal response from getOneVXOriImag operation
            */
           public void receiveResultgetOneVXOriImag(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetOneVXOriImagRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getOneVXOriImag operation
           */
            public void receiveErrorgetOneVXOriImag(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for startTask method
            * override this method for handling normal response from startTask operation
            */
           public void receiveResultstartTask(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.StartTaskRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from startTask operation
           */
            public void receiveErrorstartTask(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getAlarmCenters method
            * override this method for handling normal response from getAlarmCenters operation
            */
           public void receiveResultgetAlarmCenters(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetAlarmCentersRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getAlarmCenters operation
           */
            public void receiveErrorgetAlarmCenters(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getFaceRecord method
            * override this method for handling normal response from getFaceRecord operation
            */
           public void receiveResultgetFaceRecord(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetFaceRecordRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getFaceRecord operation
           */
            public void receiveErrorgetFaceRecord(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for setStorageVolumes method
            * override this method for handling normal response from setStorageVolumes operation
            */
           public void receiveResultsetStorageVolumes(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.SetStorageVolumesRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from setStorageVolumes operation
           */
            public void receiveErrorsetStorageVolumes(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteSnapStorDevice method
            * override this method for handling normal response from deleteSnapStorDevice operation
            */
           public void receiveResultdeleteSnapStorDevice(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.DeleteSnapStorDeviceRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteSnapStorDevice operation
           */
            public void receiveErrordeleteSnapStorDevice(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for resumeTask method
            * override this method for handling normal response from resumeTask operation
            */
           public void receiveResultresumeTask(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.ResumeTaskRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from resumeTask operation
           */
            public void receiveErrorresumeTask(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for delAlarmList method
            * override this method for handling normal response from delAlarmList operation
            */
           public void receiveResultdelAlarmList(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.DelAlarmListRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from delAlarmList operation
           */
            public void receiveErrordelAlarmList(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for addDevice method
            * override this method for handling normal response from addDevice operation
            */
           public void receiveResultaddDevice(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.AddDeviceRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addDevice operation
           */
            public void receiveErroraddDevice(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for resumeDuplicateCheck method
            * override this method for handling normal response from resumeDuplicateCheck operation
            */
           public void receiveResultresumeDuplicateCheck(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.ResumeDuplicateCheckRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from resumeDuplicateCheck operation
           */
            public void receiveErrorresumeDuplicateCheck(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for inquireRepeatedAlarm method
            * override this method for handling normal response from inquireRepeatedAlarm operation
            */
           public void receiveResultinquireRepeatedAlarm(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.InquireRepeatedAlarmRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from inquireRepeatedAlarm operation
           */
            public void receiveErrorinquireRepeatedAlarm(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for updateGroup method
            * override this method for handling normal response from updateGroup operation
            */
           public void receiveResultupdateGroup(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.UpdateGroupRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateGroup operation
           */
            public void receiveErrorupdateGroup(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for delSnapRecordByID method
            * override this method for handling normal response from delSnapRecordByID operation
            */
           public void receiveResultdelSnapRecordByID(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.DelSnapRecordByIDRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from delSnapRecordByID operation
           */
            public void receiveErrordelSnapRecordByID(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getTaskList method
            * override this method for handling normal response from getTaskList operation
            */
           public void receiveResultgetTaskList(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetTaskListRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getTaskList operation
           */
            public void receiveErrorgetTaskList(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for oneToOneCompare method
            * override this method for handling normal response from oneToOneCompare operation
            */
           public void receiveResultoneToOneCompare(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.OneToOneCompareRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from oneToOneCompare operation
           */
            public void receiveErroroneToOneCompare(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for setKeyParam method
            * override this method for handling normal response from setKeyParam operation
            */
           public void receiveResultsetKeyParam(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.SetKeyParamRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from setKeyParam operation
           */
            public void receiveErrorsetKeyParam(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for updateServer method
            * override this method for handling normal response from updateServer operation
            */
           public void receiveResultupdateServer(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.UpdateServerRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateServer operation
           */
            public void receiveErrorupdateServer(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for stopDuplicateCheck method
            * override this method for handling normal response from stopDuplicateCheck operation
            */
           public void receiveResultstopDuplicateCheck(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.StopDuplicateCheckRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from stopDuplicateCheck operation
           */
            public void receiveErrorstopDuplicateCheck(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for addSnapStorDevice method
            * override this method for handling normal response from addSnapStorDevice operation
            */
           public void receiveResultaddSnapStorDevice(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.AddSnapStorDeviceRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addSnapStorDevice operation
           */
            public void receiveErroraddSnapStorDevice(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrievalFaceRecord method
            * override this method for handling normal response from retrievalFaceRecord operation
            */
           public void receiveResultretrievalFaceRecord(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.RetrievalFaceRecordRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrievalFaceRecord operation
           */
            public void receiveErrorretrievalFaceRecord(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for addFaceRecord method
            * override this method for handling normal response from addFaceRecord operation
            */
           public void receiveResultaddFaceRecord(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.AddFaceRecordRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addFaceRecord operation
           */
            public void receiveErroraddFaceRecord(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for shutDown method
            * override this method for handling normal response from shutDown operation
            */
           public void receiveResultshutDown(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.ShutDownRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from shutDown operation
           */
            public void receiveErrorshutDown(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for timingProcDetailInfo method
            * override this method for handling normal response from timingProcDetailInfo operation
            */
           public void receiveResulttimingProcDetailInfo(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.TimingProcDetailInfoRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from timingProcDetailInfo operation
           */
            public void receiveErrortimingProcDetailInfo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for addServer method
            * override this method for handling normal response from addServer operation
            */
           public void receiveResultaddServer(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.AddServerRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addServer operation
           */
            public void receiveErroraddServer(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for delGroups method
            * override this method for handling normal response from delGroups operation
            */
           public void receiveResultdelGroups(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.DelGroupsRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from delGroups operation
           */
            public void receiveErrordelGroups(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getLisenceState method
            * override this method for handling normal response from getLisenceState operation
            */
           public void receiveResultgetLisenceState(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetLisenceStateRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getLisenceState operation
           */
            public void receiveErrorgetLisenceState(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for heartBeat method
            * override this method for handling normal response from heartBeat operation
            */
           public void receiveResultheartBeat(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.HeartBeatRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from heartBeat operation
           */
            public void receiveErrorheartBeat(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getAlarmLists method
            * override this method for handling normal response from getAlarmLists operation
            */
           public void receiveResultgetAlarmLists(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetAlarmListsRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getAlarmLists operation
           */
            public void receiveErrorgetAlarmLists(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for reBoot method
            * override this method for handling normal response from reBoot operation
            */
           public void receiveResultreBoot(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.ReBootRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from reBoot operation
           */
            public void receiveErrorreBoot(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for addSnapRecord method
            * override this method for handling normal response from addSnapRecord operation
            */
           public void receiveResultaddSnapRecord(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.AddSnapRecordRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addSnapRecord operation
           */
            public void receiveErroraddSnapRecord(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getDeviceInfo method
            * override this method for handling normal response from getDeviceInfo operation
            */
           public void receiveResultgetDeviceInfo(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetDeviceInfoRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getDeviceInfo operation
           */
            public void receiveErrorgetDeviceInfo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for pauseTask method
            * override this method for handling normal response from pauseTask operation
            */
           public void receiveResultpauseTask(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.PauseTaskRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from pauseTask operation
           */
            public void receiveErrorpauseTask(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for delSnapRecordByIP method
            * override this method for handling normal response from delSnapRecordByIP operation
            */
           public void receiveResultdelSnapRecordByIP(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.DelSnapRecordByIPRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from delSnapRecordByIP operation
           */
            public void receiveErrordelSnapRecordByIP(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for updateGroupRecord method
            * override this method for handling normal response from updateGroupRecord operation
            */
           public void receiveResultupdateGroupRecord(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.UpdateGroupRecordRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateGroupRecord operation
           */
            public void receiveErrorupdateGroupRecord(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for delGroupRecord method
            * override this method for handling normal response from delGroupRecord operation
            */
           public void receiveResultdelGroupRecord(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.DelGroupRecordRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from delGroupRecord operation
           */
            public void receiveErrordelGroupRecord(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for setAlarmCenters method
            * override this method for handling normal response from setAlarmCenters operation
            */
           public void receiveResultsetAlarmCenters(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.SetAlarmCentersRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from setAlarmCenters operation
           */
            public void receiveErrorsetAlarmCenters(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getDuplicateCheckResult method
            * override this method for handling normal response from getDuplicateCheckResult operation
            */
           public void receiveResultgetDuplicateCheckResult(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetDuplicateCheckResultRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getDuplicateCheckResult operation
           */
            public void receiveErrorgetDuplicateCheckResult(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrievalSnapRecord method
            * override this method for handling normal response from retrievalSnapRecord operation
            */
           public void receiveResultretrievalSnapRecord(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.RetrievalSnapRecordRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrievalSnapRecord operation
           */
            public void receiveErrorretrievalSnapRecord(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getSubServerNetWork method
            * override this method for handling normal response from getSubServerNetWork operation
            */
           public void receiveResultgetSubServerNetWork(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetSubServerNetWorkRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getSubServerNetWork operation
           */
            public void receiveErrorgetSubServerNetWork(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for pauseDuplicateCheck method
            * override this method for handling normal response from pauseDuplicateCheck operation
            */
           public void receiveResultpauseDuplicateCheck(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.PauseDuplicateCheckRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from pauseDuplicateCheck operation
           */
            public void receiveErrorpauseDuplicateCheck(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for addGroup method
            * override this method for handling normal response from addGroup operation
            */
           public void receiveResultaddGroup(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.AddGroupRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addGroup operation
           */
            public void receiveErroraddGroup(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for setSubServerNetWork method
            * override this method for handling normal response from setSubServerNetWork operation
            */
           public void receiveResultsetSubServerNetWork(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.SetSubServerNetWorkRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from setSubServerNetWork operation
           */
            public void receiveErrorsetSubServerNetWork(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for updateFaceDB method
            * override this method for handling normal response from updateFaceDB operation
            */
           public void receiveResultupdateFaceDB(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.UpdateFaceDBRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateFaceDB operation
           */
            public void receiveErrorupdateFaceDB(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getDuplicateCheckProgress method
            * override this method for handling normal response from getDuplicateCheckProgress operation
            */
           public void receiveResultgetDuplicateCheckProgress(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetDuplicateCheckProgressRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getDuplicateCheckProgress operation
           */
            public void receiveErrorgetDuplicateCheckProgress(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for updateDevice method
            * override this method for handling normal response from updateDevice operation
            */
           public void receiveResultupdateDevice(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.UpdateDeviceRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateDevice operation
           */
            public void receiveErrorupdateDevice(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getImportStatus method
            * override this method for handling normal response from getImportStatus operation
            */
           public void receiveResultgetImportStatus(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetImportStatusRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getImportStatus operation
           */
            public void receiveErrorgetImportStatus(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for delFaceRecord method
            * override this method for handling normal response from delFaceRecord operation
            */
           public void receiveResultdelFaceRecord(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.DelFaceRecordRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from delFaceRecord operation
           */
            public void receiveErrordelFaceRecord(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryRetrievalFaceRecord method
            * override this method for handling normal response from queryRetrievalFaceRecord operation
            */
           public void receiveResultqueryRetrievalFaceRecord(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.QueryRetrievalFaceRecordRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryRetrievalFaceRecord operation
           */
            public void receiveErrorqueryRetrievalFaceRecord(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for inquireRepeatedAlarmDetail method
            * override this method for handling normal response from inquireRepeatedAlarmDetail operation
            */
           public void receiveResultinquireRepeatedAlarmDetail(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.InquireRepeatedAlarmDetailRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from inquireRepeatedAlarmDetail operation
           */
            public void receiveErrorinquireRepeatedAlarmDetail(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryRetrievalSnapRecord method
            * override this method for handling normal response from queryRetrievalSnapRecord operation
            */
           public void receiveResultqueryRetrievalSnapRecord(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.QueryRetrievalSnapRecordRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryRetrievalSnapRecord operation
           */
            public void receiveErrorqueryRetrievalSnapRecord(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for delServer method
            * override this method for handling normal response from delServer operation
            */
           public void receiveResultdelServer(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.DelServerRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from delServer operation
           */
            public void receiveErrordelServer(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for remoteDisARM method
            * override this method for handling normal response from remoteDisARM operation
            */
           public void receiveResultremoteDisARM(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.RemoteDisARMRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from remoteDisARM operation
           */
            public void receiveErrorremoteDisARM(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for delDuplicateCheckResult method
            * override this method for handling normal response from delDuplicateCheckResult operation
            */
           public void receiveResultdelDuplicateCheckResult(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.DelDuplicateCheckResultRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from delDuplicateCheckResult operation
           */
            public void receiveErrordelDuplicateCheckResult(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for setSubServerKeyParam method
            * override this method for handling normal response from setSubServerKeyParam operation
            */
           public void receiveResultsetSubServerKeyParam(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.SetSubServerKeyParamRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from setSubServerKeyParam operation
           */
            public void receiveErrorsetSubServerKeyParam(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getFaceRecords method
            * override this method for handling normal response from getFaceRecords operation
            */
           public void receiveResultgetFaceRecords(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetFaceRecordsRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getFaceRecords operation
           */
            public void receiveErrorgetFaceRecords(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for timingCmpProc method
            * override this method for handling normal response from timingCmpProc operation
            */
           public void receiveResulttimingCmpProc(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.TimingCmpProcRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from timingCmpProc operation
           */
            public void receiveErrortimingCmpProc(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getSnapRecord method
            * override this method for handling normal response from getSnapRecord operation
            */
           public void receiveResultgetSnapRecord(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetSnapRecordRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getSnapRecord operation
           */
            public void receiveErrorgetSnapRecord(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for outputEleProtectFile method
            * override this method for handling normal response from outputEleProtectFile operation
            */
           public void receiveResultoutputEleProtectFile(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.OutputEleProtectFileRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from outputEleProtectFile operation
           */
            public void receiveErroroutputEleProtectFile(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for delDevices method
            * override this method for handling normal response from delDevices operation
            */
           public void receiveResultdelDevices(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.DelDevicesRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from delDevices operation
           */
            public void receiveErrordelDevices(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getServers method
            * override this method for handling normal response from getServers operation
            */
           public void receiveResultgetServers(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetServersRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getServers operation
           */
            public void receiveErrorgetServers(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for setDeviceInfo method
            * override this method for handling normal response from setDeviceInfo operation
            */
           public void receiveResultsetDeviceInfo(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.SetDeviceInfoRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from setDeviceInfo operation
           */
            public void receiveErrorsetDeviceInfo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getRetrievalProcProgress method
            * override this method for handling normal response from getRetrievalProcProgress operation
            */
           public void receiveResultgetRetrievalProcProgress(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetRetrievalProcProgressRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getRetrievalProcProgress operation
           */
            public void receiveErrorgetRetrievalProcProgress(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for delDuplicateCheckTask method
            * override this method for handling normal response from delDuplicateCheckTask operation
            */
           public void receiveResultdelDuplicateCheckTask(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.DelDuplicateCheckTaskRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from delDuplicateCheckTask operation
           */
            public void receiveErrordelDuplicateCheckTask(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getBlackAlarmTraceList method
            * override this method for handling normal response from getBlackAlarmTraceList operation
            */
           public void receiveResultgetBlackAlarmTraceList(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetBlackAlarmTraceListRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getBlackAlarmTraceList operation
           */
            public void receiveErrorgetBlackAlarmTraceList(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for delDevice method
            * override this method for handling normal response from delDevice operation
            */
           public void receiveResultdelDevice(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.DelDeviceRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from delDevice operation
           */
            public void receiveErrordelDevice(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getSnapStorDevice method
            * override this method for handling normal response from getSnapStorDevice operation
            */
           public void receiveResultgetSnapStorDevice(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetSnapStorDeviceRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getSnapStorDevice operation
           */
            public void receiveErrorgetSnapStorDevice(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for relateDeviceAndGroup method
            * override this method for handling normal response from relateDeviceAndGroup operation
            */
           public void receiveResultrelateDeviceAndGroup(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.RelateDeviceAndGroupRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from relateDeviceAndGroup operation
           */
            public void receiveErrorrelateDeviceAndGroup(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getKeyParam method
            * override this method for handling normal response from getKeyParam operation
            */
           public void receiveResultgetKeyParam(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetKeyParamRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getKeyParam operation
           */
            public void receiveErrorgetKeyParam(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for remoteARM method
            * override this method for handling normal response from remoteARM operation
            */
           public void receiveResultremoteARM(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.RemoteARMRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from remoteARM operation
           */
            public void receiveErrorremoteARM(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for addFaceRec2GroupRec method
            * override this method for handling normal response from addFaceRec2GroupRec operation
            */
           public void receiveResultaddFaceRec2GroupRec(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.AddFaceRec2GroupRecRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addFaceRec2GroupRec operation
           */
            public void receiveErroraddFaceRec2GroupRec(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getGroupRecords method
            * override this method for handling normal response from getGroupRecords operation
            */
           public void receiveResultgetGroupRecords(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetGroupRecordsRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getGroupRecords operation
           */
            public void receiveErrorgetGroupRecords(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getGroups method
            * override this method for handling normal response from getGroups operation
            */
           public void receiveResultgetGroups(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetGroupsRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getGroups operation
           */
            public void receiveErrorgetGroups(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for delServers method
            * override this method for handling normal response from delServers operation
            */
           public void receiveResultdelServers(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.DelServersRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from delServers operation
           */
            public void receiveErrordelServers(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for inputEleProtectFile method
            * override this method for handling normal response from inputEleProtectFile operation
            */
           public void receiveResultinputEleProtectFile(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.InputEleProtectFileRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from inputEleProtectFile operation
           */
            public void receiveErrorinputEleProtectFile(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getTaskResult method
            * override this method for handling normal response from getTaskResult operation
            */
           public void receiveResultgetTaskResult(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetTaskResultRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getTaskResult operation
           */
            public void receiveErrorgetTaskResult(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for delAllTask method
            * override this method for handling normal response from delAllTask operation
            */
           public void receiveResultdelAllTask(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.DelAllTaskRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from delAllTask operation
           */
            public void receiveErrordelAllTask(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getSnapRecords method
            * override this method for handling normal response from getSnapRecords operation
            */
           public void receiveResultgetSnapRecords(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetSnapRecordsRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getSnapRecords operation
           */
            public void receiveErrorgetSnapRecords(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getSubServerKeyParam method
            * override this method for handling normal response from getSubServerKeyParam operation
            */
           public void receiveResultgetSubServerKeyParam(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetSubServerKeyParamRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getSubServerKeyParam operation
           */
            public void receiveErrorgetSubServerKeyParam(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for delGroup method
            * override this method for handling normal response from delGroup operation
            */
           public void receiveResultdelGroup(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.DelGroupRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from delGroup operation
           */
            public void receiveErrordelGroup(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for configDatabase method
            * override this method for handling normal response from configDatabase operation
            */
           public void receiveResultconfigDatabase(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.ConfigDatabaseRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from configDatabase operation
           */
            public void receiveErrorconfigDatabase(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for updateFaceRecord method
            * override this method for handling normal response from updateFaceRecord operation
            */
           public void receiveResultupdateFaceRecord(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.UpdateFaceRecordRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateFaceRecord operation
           */
            public void receiveErrorupdateFaceRecord(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getDevices method
            * override this method for handling normal response from getDevices operation
            */
           public void receiveResultgetDevices(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetDevicesRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getDevices operation
           */
            public void receiveErrorgetDevices(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for startImportTask method
            * override this method for handling normal response from startImportTask operation
            */
           public void receiveResultstartImportTask(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.StartImportTaskRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from startImportTask operation
           */
            public void receiveErrorstartImportTask(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getDatabaseConfig method
            * override this method for handling normal response from getDatabaseConfig operation
            */
           public void receiveResultgetDatabaseConfig(
                    cn.ffcs.zhsq.times.stub.HFSNet.WebServiceStub.GetDatabaseConfigRsp result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getDatabaseConfig operation
           */
            public void receiveErrorgetDatabaseConfig(java.lang.Exception e) {
            }
                


    }
    