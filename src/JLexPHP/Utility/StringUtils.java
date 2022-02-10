/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JLexPHP.Utility;

/**
 *
 * @author rnapoles
 */
public class StringUtils {
 
  public static String ucfirst(String string)
  {

    if (string == null)
      return new String();
    else if (string.length() == 0)
      return string;

    StringBuilder sb = new StringBuilder();
    sb = sb.append(Character.toUpperCase(string.charAt(0)));
    sb = sb.append(string, 1, string.length());
    
    return sb.toString();
  }    
    
}
