configuration HomeSenseAppC
{
}
implementation
{

 components new TimerMilliC() as PIRInput;
 components MainC, HomeSenseC as App, LedsC;
 components ActiveMessageC as AMsg;
 components HplMsp430GeneralIOC;

 App.Boot -> MainC.Boot;
 App.Leds -> LedsC;
 App.PIRInput -> PIRInput;
 App.Control -> AMsg;
 App.ADC6_PIN2  -> HplMsp430GeneralIOC.Port67;

 App.AMSend-> AMsg.AMSend[AM_MOTE_STATUS_MSG];
}
