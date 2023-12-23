package cn.tekin.json;

import java.util.HashMap;

public class JSONParse {
   private static int spaceLen(String json) {
      return json.length() - json.trim().length();
   }

   private static ParseResult ParseDict(String json) {
      JSONObject obj = new JSONObject(JSONType.Dict, new HashMap());
      int len = 1;
      len = len + spaceLen(json.substring(len));

      while(true) {
         if (json.charAt(len) == '}') {
            ++len;
            len += spaceLen(json.substring(len));
            break;
         }

         ParseResult key = ParseString(json.substring(len));
         len += key.len;
         len += spaceLen(json.substring(len));
         if (json.charAt(len) != ':') {
            throw new RuntimeException("Invalid JSON");
         }

         ++len;
         ParseResult value = parse(json.substring(len));
         len += value.len;
         len += spaceLen(json.substring(len));
         obj.set(key.obj.string, value.obj);
         if (json.charAt(len) == '}') {
            ++len;
            len += spaceLen(json.substring(len));
            break;
         }

         if (json.charAt(len) != ',') {
            throw new RuntimeException("Invalid JSON");
         }

         ++len;
         len += spaceLen(json.substring(len));
      }

      return new ParseResult(len, obj);
   }

   private static ParseResult ParseString(String json) {
      int len = 1;

      StringBuilder sb;
      for(sb = new StringBuilder(); json.charAt(len) != '"'; ++len) {
         if (json.charAt(len) == '\\') {
            ++len;
            switch(json.charAt(len)) {
            case '"':
               sb.append('"');
               break;
            case '/':
               sb.append('/');
               break;
            case '\\':
               sb.append('\\');
               break;
            case 'b':
               sb.append('\b');
               break;
            case 'f':
               sb.append('\f');
               break;
            case 'n':
               sb.append('\n');
               break;
            case 'r':
               sb.append('\r');
               break;
            case 't':
               sb.append('\t');
               break;
            case 'u':
               sb.append((char)Integer.parseInt(json.substring(len + 1, len + 5), 16));
               len += 4;
               break;
            default:
               throw new RuntimeException("Invalid JSON");
            }
         } else {
            sb.append(json.charAt(len));
         }
      }

      ++len;
      return new ParseResult(len, new JSONObject(JSONType.String, sb.toString()));
   }

   private static ParseResult ParseList(String json) {
      JSONObject[] list = new JSONObject[0];
      int len = 1;

      while(true) {
         if (json.charAt(len) == ']') {
            ++len;
            len += spaceLen(json.substring(len));
            break;
         }

         ParseResult value = parse(json.substring(len));
         len += value.len;
         len += spaceLen(json.substring(len));
         JSONObject[] newList = new JSONObject[list.length + 1];
         System.arraycopy(list, 0, newList, 0, list.length);
         newList[list.length] = value.obj;
         list = newList;
         if (json.charAt(len) == ']') {
            ++len;
            len += spaceLen(json.substring(len));
            break;
         }

         if (json.charAt(len) != ',') {
            throw new RuntimeException("Invalid JSON");
         }

         ++len;
         len += spaceLen(json.substring(len));
      }

      return new ParseResult(len, new JSONObject(JSONType.List, list));
   }

   private static ParseResult ParseNumber(String json) {
      int len = 0;
      StringBuilder sb = new StringBuilder();

      while(true) {
         if (json.charAt(len) != '-' && json.charAt(len) != '+' && json.charAt(len) != 'e' && json.charAt(len) != 'E' && json.charAt(len) != '.') {
            if ('0' > json.charAt(len) || json.charAt(len) > '9') {
               return new ParseResult(len, new JSONObject(JSONType.Number, sb.toString()));
            }

            sb.append(json.charAt(len));
         } else {
            sb.append(json.charAt(len));
         }

         ++len;
      }
   }

   private static ParseResult ParseNull(String json) {
      return new ParseResult(4, new JSONObject(JSONType.Null, (Object)null));
   }

   private static ParseResult ParseBoolean(String json) {
      return new ParseResult(json.startsWith("true") ? 4 : 5, new JSONObject(JSONType.Boolean, json.startsWith("true")));
   }

   private static ParseResult parse(String json) {
      int slen = spaceLen(json);
      json = json.substring(slen);
      if (json.length() == 0) {
         return null;
      } else {
         ParseResult result;
         if (json.startsWith("{")) {
            result = ParseDict(json);
         } else if (json.startsWith("[")) {
            result = ParseList(json);
         } else if (json.startsWith("\"")) {
            result = ParseString(json);
         } else if ('0' <= json.charAt(0) && json.charAt(0) <= '9') {
            result = ParseNumber(json);
         } else if (json.startsWith("null")) {
            result = ParseNull(json);
         } else {
            if (!json.startsWith("true") && !json.startsWith("false")) {
               throw new RuntimeException("Invalid JSON");
            }

            result = ParseBoolean(json);
         }

         result.AddLen(slen + spaceLen(json.substring(result.len)));
         return result;
      }
   }

   public static JSONObject Parse(String json) {
      json = json.trim().replace("\n", "").replace("\r", "");
      if (json.length() == 0) {
         return null;
      } else {
         ParseResult result = parse(json);
         if (result.len != json.length()) {
            throw new RuntimeException("Invalid JSON");
         } else {
            return result.obj;
         }
      }
   }
}
