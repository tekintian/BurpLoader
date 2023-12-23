package cn.tekin.json;

public class ParseResult {
   public int len;
   public JSONObject obj;

   public ParseResult(int len, JSONObject obj) {
      this.len = len;
      this.obj = obj;
   }

   public void AddLen(int len) {
      this.len += len;
   }
}
