package com.appota.app.spinmachine.object;

import java.io.Serializable;

public class AccessToken
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private int errorCode;
  private String expireDate;
  private String refreshToken;
  private String token;

  public AccessToken()
  {
  }

  public AccessToken(String paramString)
  {
    this.token = paramString;
  }

  public AccessToken(String paramString1, String paramString2, String paramString3)
  {
    this.token = paramString1;
    this.refreshToken = paramString2;
    this.expireDate = paramString3;
  }

  public int getErrorCode()
  {
    return this.errorCode;
  }

  public String getExpireDate()
  {
    return this.expireDate;
  }

  public String getRefreshToken()
  {
    return this.refreshToken;
  }

  public String getToken()
  {
    return this.token;
  }

  public void setErrorCode(int paramInt)
  {
    this.errorCode = paramInt;
  }

  public void setExpireDate(String paramString)
  {
    this.expireDate = paramString;
  }

  public void setRefreshToken(String paramString)
  {
    this.refreshToken = paramString;
  }

  public void setToken(String paramString)
  {
    this.token = paramString;
  }
}

/* Location:           E:\Desktop4ReinstallWin\dex2jar-0.0.9.8\AppStoreVn2.1.3_dex2jar.jar
 * Qualified Name:     com.appstore.vn.model.AccessToken
 * JD-Core Version:    0.6.2
 */