
/**
 * SXDataPoolWebServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package cn.ffcs.zhsq.event.service.stub.yanping.szcg;

    /**
     *  SXDataPoolWebServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class SXDataPoolWebServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public SXDataPoolWebServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public SXDataPoolWebServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for sendToCenterByZWW method
            * override this method for handling normal response from sendToCenterByZWW operation
            */
           public void receiveResultsendToCenterByZWW(
                    SXDataPoolWebServiceStub.SendToCenterByZWWResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from sendToCenterByZWW operation
           */
            public void receiveErrorsendToCenterByZWW(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getClosedFeedback method
            * override this method for handling normal response from getClosedFeedback operation
            */
           public void receiveResultgetClosedFeedback(
                    SXDataPoolWebServiceStub.GetClosedFeedbackResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getClosedFeedback operation
           */
            public void receiveErrorgetClosedFeedback(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for sendToCenter method
            * override this method for handling normal response from sendToCenter operation
            */
           public void receiveResultsendToCenter(
                    SXDataPoolWebServiceStub.SendToCenterResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from sendToCenter operation
           */
            public void receiveErrorsendToCenter(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for archiveNotice method
            * override this method for handling normal response from archiveNotice operation
            */
           public void receiveResultarchiveNotice(
                    SXDataPoolWebServiceStub.ArchiveNoticeResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from archiveNotice operation
           */
            public void receiveErrorarchiveNotice(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for receivedClosed method
            * override this method for handling normal response from receivedClosed operation
            */
           public void receiveResultreceivedClosed(
                    SXDataPoolWebServiceStub.ReceivedClosedResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from receivedClosed operation
           */
            public void receiveErrorreceivedClosed(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getDayCount method
            * override this method for handling normal response from getDayCount operation
            */
           public void receiveResultgetDayCount(
                    SXDataPoolWebServiceStub.GetDayCountResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getDayCount operation
           */
            public void receiveErrorgetDayCount(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for eventFeedback method
            * override this method for handling normal response from eventFeedback operation
            */
           public void receiveResulteventFeedback(
                    SXDataPoolWebServiceStub.EventFeedbackResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from eventFeedback operation
           */
            public void receiveErroreventFeedback(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getEventList method
            * override this method for handling normal response from getEventList operation
            */
           public void receiveResultgetEventList(
                    SXDataPoolWebServiceStub.GetEventListResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getEventList operation
           */
            public void receiveErrorgetEventList(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for postponeFeedback method
            * override this method for handling normal response from postponeFeedback operation
            */
           public void receiveResultpostponeFeedback(
                    SXDataPoolWebServiceStub.PostponeFeedbackResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from postponeFeedback operation
           */
            public void receiveErrorpostponeFeedback(Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for postponeApplication method
            * override this method for handling normal response from postponeApplication operation
            */
           public void receiveResultpostponeApplication(
                    SXDataPoolWebServiceStub.PostponeApplicationResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from postponeApplication operation
           */
            public void receiveErrorpostponeApplication(Exception e) {
            }
                


    }
    