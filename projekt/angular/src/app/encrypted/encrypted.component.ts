import { Component, OnInit } from '@angular/core';
import {EncryptedService} from "../service/encrypted.service";
import {Discovery} from "../discovery";
import {DataService} from "../service/data.service";
import {KeyService} from "../service/key.service";
import {SignContent} from "../signcontent";

@Component({
  selector: 'app-encrypted',
  templateUrl: './encrypted.component.html',
  styleUrls: ['./encrypted.component.css']
})
export class EncryptedComponent implements OnInit {

  private publicKeyB64: string;
  private signedContent: string;
  private signatureB64: string;
  private discoveries:Discovery[];

  constructor(private encryptedService: EncryptedService, private keyService:KeyService) {
  }
  private signContent:SignContent;

  ngOnInit() {
    this.getContentAndSignature();
  }

  public chooseFile(){
    document.getElementById("inputFile").click();
  }

  public getContentAndSignature() {
    /*this.encryptedService.getContentAndSignature().subscribe((x) => {
      this.signContent=x;
      console.log(this.signContent.signature);
      this.signatureB64 = JSON.stringify(x.signature);
      this.signatureB64=this.signatureB64.slice(1,this.signatureB64.length-1);
      this.signedContent = JSON.stringify(x.signedContent);
    });*/
  }

//convert public key, data and signature to ArrayBuffer.

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

  changeEvent(event) {
    let input=<HTMLInputElement>(document.getElementById("nameOfFile"));
    let file=event.target.files[0];
    input.value=file.name;
    this.readFile(file);
  }
  private readFile(file:any){
    let myReader: FileReader = new FileReader();
    myReader.readAsText(file);
    myReader.onloadend = (e)=> {
      this.publicKeyB64=myReader.result;
      this.keyService.addBehaviourSource(this.publicKeyB64);
      //this.checkContent();
    };
  }
}
