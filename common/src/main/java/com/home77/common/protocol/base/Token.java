package com.home77.common.protocol.base;

import com.google.gson.annotations.SerializedName;

import static com.home77.common.protocol.base.Constants.SEPARATOR;

public final class Token {
  // static

  public static Token make(String token, Long vno, String sign) {
    final Token t = new Token();
    t.mToken = token;
    t.mVno = vno;
    t.mSign = sign;
    return t;
  }

  // class

  @SerializedName("token")
  private String mToken;

  @SerializedName("vno")
  private Long mVno;

  @SerializedName("sign")
  private String mSign;

  private Token() {
  }

  // getter

  public String token() {
    return mToken;
  }

  public long vno() {
    return mVno;
  }

  public String sign() {
    return mSign;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("token=").append(mToken);
    sb.append(SEPARATOR);
    sb.append("vno=").append(mVno);
    return sb.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Token)) {
      return false;
    }

    Token that = (Token) obj;
    return this.mToken.equals(that.mToken) &&
           this.mVno.equals(that.mVno) &&
           this.mSign.equals(that.mSign);
  }
}
