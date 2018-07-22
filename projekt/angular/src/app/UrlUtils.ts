import {BaseUrl} from "./baseurl.enum";
export class UrlUtils {
  public static addParameterToUrl(url:string,parameter:string,value:any):string{
    if(url.endsWith("/")){
      url=url.replace(/.$/,"");
    }
    if(url.includes("?")){
      url+="&"+parameter+"="+value;
    }else{
      url+="?"+parameter+"="+value;
    }
    return url;
  }
  public static deleteParameterFromUrl(url:string,nameOfParameter:string):string{
    let regExp=new RegExp('[?&]'+nameOfParameter+'=.[^&?]*');
    if(url.match(regExp)!==null){
      let splittedUrl=url.split(regExp);
      if(!splittedUrl[0].includes('?')){
        splittedUrl[1].replace('&','?');
      }
      return splittedUrl.join('');
    }
    return url;
  }
  public static checkUrlWithRegex(url:string, regexp:string):boolean{
    let regExp:RegExp=new RegExp(regexp);
    return regExp.test(url);
  }
  public static generateBasicUrl(protocol:string):string{
    return protocol+"//"+BaseUrl.development;
  }
}
