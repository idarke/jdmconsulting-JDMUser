package com.jdm.handler;

import com.jdm.beans.InputPayloadBean;

public interface IHandler
{
   Object handlePayload(InputPayloadBean inputPayloadBean);
}
