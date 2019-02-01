import {Injectable} from "@angular/core";
import {Discovery} from "../model/discovery";
import {SignContent} from "../model/signcontent";

@Injectable()
export class EncryptedService {

  private publicKeyB64: string;
  private signedContent: string;
  private signatureB64: string;
  private discoveries:Discovery[];
  constructor() { }

  public checkIfSignedContentConsistentWithSignature(signContent:SignContent){
    this.signatureB64=JSON.stringify(signContent.signature);
    this.signedContent=JSON.stringify(signContent.signedContent);
    this.checkContent();
  }
  checkContent() {
    let signatureAlgorithm = {
      name: 'RSASSA-PKCS1-v1_5',
      modulusLength: 2048,
      publicExponent: new Uint8Array([0x01, 0x00, 0x01]),
      hash: {name: 'SHA-256'}
    };
    let publicKey = this.str2ab(atob(this.publicKeyB64));
    let data=this.str2ab(this.signedContent);
    let signature = this.str2ab(atob(this.signatureB64));
    try {
      crypto.subtle.importKey("spki", publicKey, signatureAlgorithm, false, ["verify"]).then((key)=> {
        console.log(key);
        return crypto.subtle.verify(signatureAlgorithm, key, signature, data);
      }).then((valid)=> {
        console.log("Signature valid: " + valid);
        this.discoveries=JSON.parse(this.signedContent);
        console.log(this.discoveries);
      });
    } catch (err) {
      alert("Verification failed " + err);
    }
  }

  str2ab(str): Uint8Array {
    var arrBuff = new ArrayBuffer(str.length);
    var bytes = new Uint8Array(arrBuff);
    for (var iii = 0; iii < str.length; iii++) {
      bytes[iii] = str.charCodeAt(iii);
    }
    return bytes;
  }


}
