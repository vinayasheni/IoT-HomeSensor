#include "MoteStatus.h"

configuration HomeSenseBaseAppC
{

}

implementation
{
 components MainC, HomeSenseBaseC as App,LedsC;
 components new TimerMilliC() as PIRInput;
 components SerialActiveMessageC as SAM;
 components ActiveMessageC as AM;
 components new AMReceiverC(AM_MOTE_STATUS_MSG);
 App.Boot -> MainC.Boot;
 App.Leds -> LedsC; 
 App.Control -> AM;
 App.Control -> SAM;
 App.AMSend -> SAM.AMSend[AM_MOTE_STATUS_MSG];
 App.PIRInput -> PIRInput;
 //App.Receive -> SAM.Receive[AM_MOTE_STATUS_MSG];
 App.Receive -> AMReceiverC; 
}
