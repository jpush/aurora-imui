import React, { Component } from 'react'

import {
  StyleSheet,
  View,
  Alert,
  Dimensions,
  Button,
  Platform,
} from 'react-native'

var RNFS = require('react-native-fs')

var ReactNative = require('react-native')
import IMUI from 'aurora-imui-react-native'
var InputView = IMUI.ChatInput
var MessageListView = IMUI.MessageList
const AuroraIController = IMUI.AuroraIMUIController
const window = Dimensions.get('window')


var themsgid = 1

function constructNormalMessage() {

  var message = {}
  message.msgId = themsgid.toString()
  themsgid += 1
  message.status = "send_succeed"
  message.isOutgoing = true
  var date = new Date()
  message.timeString = date.getHours() + ":" + date.getMinutes()
  var user = {
    userId: "",
    displayName: "replace your nickname",
    avatarPath: "images"
  }
  if (Platform.OS === "ios") {
    user.avatarPath = RNFS.MainBundlePath + '/default_header.png'
  }
  message.fromUser = user

  return message
}

var imageUrlArray = [
  //        "https://www.lsc-online.com/wp-content/uploads/2017/05/bigstock-151229657.jpg"
  //        "https://www.iconspng.com/images/coniglio-rabbit-small/coniglio-rabbit-small.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926548887&di=f107f4f8bd50fada6c5770ef27535277&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F11%2F67%2F23%2F69i58PICP37.jpg",//1
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926367926&di=ac707ee3e73241daaa5598730d28909d&imgtype=0&src=http%3A%2F%2Fimg.25pp.com%2Fuploadfile%2Fapp%2Ficon%2F20160220%2F1455956985275086.jpg",//2
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926419519&di=c545a5d3310e88454d222623532e06b7&imgtype=0&src=http%3A%2F%2Fimg.25pp.com%2Fuploadfile%2Fyouxi%2Fimages%2F2015%2F0701%2F20150701085247270.jpg",//3
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926596720&di=001e99492a3e684a63c07b204ff1c641&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01567057a188f70000018c1bc79411.jpg%40900w_1l_2o_100sh.jpg",//4
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926617378&di=01ade16186d4f0b6ef4fead945d142c4&imgtype=0&src=http%3A%2F%2Fimg1.tplm123.com%2F2008%2F04%2F04%2F3421%2F2309912507054.jpg",//5
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926710881&di=83ecd418f598bcadb9d74e5075397fc2&imgtype=0&src=http%3A%2F%2Fwww.missku.com%2Fd%2Ffile%2Fimport%2F2015%2F1211%2Fthumb_20151211142740226.jpg",//6
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926731796&di=e431578738f709fd75f17799a91ac4a9&imgtype=0&src=http%3A%2F%2Ff.hiphotos.baidu.com%2Fbaike%2Fw%253D268%2Fsign%3D4c99e09935d3d539c13d08c50286e927%2F8c1001e93901213f3d7d8ebb57e736d12f2e950f.jpg",//7
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926752612&di=7a8d887ece70f73517b32803a2e048cd&imgtype=0&src=http%3A%2F%2Fimg10.360buyimg.com%2FpopWaterMark%2Fg15%2FM01%2F03%2F13%2FrBEhWVLh4JEIAAAAAAB99-puGocAAIKSwLztRsAAH4P213.jpg",//8
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926795383&di=1ce1c07257fa6918c4fbbeb3ee4e1eef&imgtype=0&src=http%3A%2F%2Fd.ifengimg.com%2Fw600%2Fp0.ifengimg.com%2Fpmop%2F2018%2F0322%2FB02F8FEE6DF6ECD3358F1EB877ECABC93268790E_size31_w643_h643.jpeg",//9
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926793631&di=76964940e9b139ec8960ebf3dc360c8c&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20170828%2F84c750b9293744549a169ae3d80a0dab.jpeg",//10
  "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=898198915,303815663&fm=200&gp=0.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936168009&di=89ba45a63f42525678093902e46f4a91&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fb58f8c5494eef01fd65fb3feebfe9925bc317d46.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936168009&di=b2880349c31f7b6e8f99c2804f6aab1c&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F54fbb2fb43166d22c5dd83a84d2309f79052d260.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936168009&di=c87b27ba6616c97573da2f4bb6feb705&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F242dd42a2834349beaa73bb9c2ea15ce36d3be6d.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936168008&di=bea720e169fa4455596c46040cbf44af&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F5ab5c9ea15ce36d3e9f1a40c31f33a87e950b110.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936168008&di=12d6a78b3e47ebb59166ae6d835c080d&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fdbb44aed2e738bd4ff4b3d0aaa8b87d6277ff9a7.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936168008&di=eacdfe43ba6779092176a676dd1384d0&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F32fa828ba61ea8d30fe6f43f9c0a304e241f58c1.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936168007&di=985df3a5cb77f6aa2c7bbdb468810815&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fae51f3deb48f8c5449425b1531292df5e0fe7f73.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936168007&di=34f235219fc292a2bb22d1e96c4d0f75&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F472309f7905298220c6eb9f8dcca7bcb0a46d4a8.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936168007&di=cc14ec3efe584b256a817c0b4fe81dac&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F14ce36d3d539b600eb06458ce250352ac75cb7c5.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936168007&di=edfe4269d7a5917e9001f8404a7eaab1&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F242dd42a2834349b803f9da3c2ea15ce36d3bedf.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936168007&di=480cd8787f333e449f6e2e721c105ac7&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Faec379310a55b3194dd9fd4e48a98226cffc17ec.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936168007&di=96e8d3bcc5ac0477afaf85fa43a1b117&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F2fdda3cc7cd98d10261a710a2a3fb80e7bec903a.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936168006&di=b8bdee3e59806b81dcbbe62422b611df&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fd31b0ef41bd5ad6ebee538748bcb39dbb6fd3c0b.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936168006&di=75091da17db8f68521a583b18908e77b&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F86d6277f9e2f07084a6bb43ae224b899a901f29a.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936168005&di=16c0489635d510e2f96124098b75fb53&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F8b82b9014a90f60313ce337c3212b31bb051ed8b.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936168005&di=1819cb1cd47b77c5f95835acf599e6ee&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F8d5494eef01f3a296283dcf49225bc315c607c44.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936168005&di=df2517dd1b3c385e873f5150b7f27fe4&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Faa18972bd40735fafa1dc7be94510fb30e2408c3.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936168005&di=fa0a503e2982b4ebadbd58de719611f5&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F71cf3bc79f3df8dcd6c60aaac611728b47102815.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936168005&di=f29134593847d3d92639724f6b0ead8b&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F267f9e2f070828387eef442eb399a9014c08f12e.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936168005&di=2f27fd7f3489a50953dd5f7da7447d54&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fadaf2edda3cc7cd9ac2b4ce23201213fb80e9182.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936168005&di=a7575a17cf24fc9db0a04481da53289a&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F0df3d7ca7bcb0a46b89251306163f6246b60af03.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936168004&di=0cfa99e930e61855d62c773b5c9592ff&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F0ff41bd5ad6eddc481c47dc032dbb6fd526633ee.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936168004&di=fe53b7f82db98b1aaf589d0afcc3708c&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F1f178a82b9014a901cb67c86a2773912b31bee05.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936168004&di=ac7c707455596db6f04df0279510df31&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fd4628535e5dde7114233daccacefce1b9d166123.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936168004&di=61d04950d70b066919994396297594a0&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F29381f30e924b899abf1430365061d950a7bf63f.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936386988&di=7b42bfdbb17b04ff05a18641414454a0&imgtype=jpg&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20170530%2F4d0c9888924347d8a162a10499dff841_th.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936168003&di=6f52747c2c0784b95a2453e9decf6e1d&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Ffcfaaf51f3deb48f9077f840fa1f3a292df57815.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936168002&di=6663ef8c02e5614f577ec67861ff8fd8&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F503d269759ee3d6dd37811e348166d224f4ade58.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936168002&di=824d8714f644af10e4c494fe358427ed&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F8d5494eef01f3a298fef0a539325bc315c607c0c.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936168001&di=941f731fc00b6bf825523e4707accbc2&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F0824ab18972bd40737e733fa71899e510fb30990.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936168001&di=534a46fce67e8cdecf4e0c3f50be085c&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F8435e5dde71190efe0684d42c41b9d16fcfa6098.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534936168001&di=a7da0ca4ea58b2c76893bde8f81530fd&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F8718367adab44aeda155fed8b81c8701a18bfbf7.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1535538284&di=30dce1e0b21e091fe9dfa094454b734b&imgtype=jpg&er=1&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fd31b0ef41bd5ad6ebee538748bcb39dbb6fd3c0b.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534943565170&di=ac0dbe647fbceb6b371536a603864ca0&imgtype=0&src=http%3A%2F%2Fwww.flipgeeks.com%2Fwp-content%2Fuploads%2F2015%2F04%2FBloodborne-Screenshot-PS4.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534943565170&di=7b7579b6f611e26447b98d8a08f955c9&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F7aec54e736d12f2edf8d8a5b44c2d56285356804.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534943565170&di=012262360cdb0ce082978775376c1716&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F7af40ad162d9f2d337faade6a3ec8a136227ccd4.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534943565167&di=2c68a71e7a0e1fc966a99ad7374974b8&imgtype=0&src=http%3A%2F%2Fclearwaterbeachportraits.com%2Fwp-content%2Fuploads%2Fgalleries%2Fpost-19%2Fclearwater-beach-photography-149.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534943565167&di=0c0c7d56c4022af834c9fb94d0c70d12&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Ff3d3572c11dfa9ecca64bd2d69d0f703918fc146.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534943565166&di=aa35cc8242fd4dc57d9b40ee2f0e9e3a&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Feac4b74543a98226eef4ca208082b9014a90eb84.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534943565166&di=bdf2c8cb95591f1a5fa1a09e37995e3e&imgtype=0&src=http%3A%2F%2Fimage-bugs.com%2Fimages%2Fana_braga_stretch_nice_8.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534943652782&di=382979eb84179c39608b78f301c7876b&imgtype=jpg&src=http%3A%2F%2Fimg2.imgtn.bdimg.com%2Fit%2Fu%3D2898115912%2C296134068%26fm%3D214%26gp%3D0.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534943565164&di=8df57fe288facf29c71f56bcf15b7cff&imgtype=0&src=http%3A%2F%2Fstatic1.bigstockphoto.com%2Fthumbs%2F6%2F3%2F1%2Flarge1500%2F136541063.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534943668136&di=980b16fac4be76232e635fefd2945848&imgtype=jpg&src=http%3A%2F%2Fimg2.imgtn.bdimg.com%2Fit%2Fu%3D1228727337%2C958709662%26fm%3D214%26gp%3D0.jpg",
  "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2719433431,295832179&fm=11&gp=0.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534943565163&di=39a73a8f4ac72ba058aa2cbbf0ca4f0d&imgtype=0&src=http%3A%2F%2Fccnwpcms.oss-cn-beijing.aliyuncs.com%2Fwp-upload%2F2015%2F12%2Fimage1_recompress.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534943632003&di=1a49da9167f09020deccc4f69f815065&imgtype=0&src=http%3A%2F%2Fimage-bugs.com%2Fimages%2Fcourtney_robertson_car_wash_6.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534943719709&di=24cd0590d6e252ad20a227f181f21e26&imgtype=jpg&src=http%3A%2F%2Fimg2.imgtn.bdimg.com%2Fit%2Fu%3D1647195362%2C1638863086%26fm%3D214%26gp%3D0.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534943732372&di=b63011308816d08380fe0948ad52b571&imgtype=jpg&src=http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D2457068298%2C2167463289%26fm%3D214%26gp%3D0.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534943631996&di=4f69fbf568a1f71287a20ef726742240&imgtype=0&src=http%3A%2F%2Fpic.baa.bitautotech.com%2Fimg%2FV2pic.baa.bitautotech.com%2Fspace%2F2013%2F09%2F11%2F16efc810-68e8-47a2-8dd6-d525223cfa90_735_0_max_jpg.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534943746999&di=6a1511e18236274e88332592f0d793ba&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F04e74855473fd60000019ae9cbb3d3.jpg",
  "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3902983896,2139189367&fm=26&gp=0.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534943746998&di=d0cbd3329e227066155897f52f3b9604&imgtype=0&src=http%3A%2F%2Fwww.aftertech.eu%2Febayimages%2F120722409634%2Fimg7460l.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534943746996&di=ad157c6f6cf0559b272718793c5af048&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fphotoblog%2F1206%2F21%2Fc2%2F12078509_12078509_1340246370201.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534943746996&di=ad157c6f6cf0559b272718793c5af048&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fphotoblog%2F1206%2F21%2Fc2%2F12078509_12078509_1340246370201.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534943746996&di=ad157c6f6cf0559b272718793c5af048&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fphotoblog%2F1206%2F21%2Fc2%2F12078509_12078509_1340246370201.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534943746996&di=ad157c6f6cf0559b272718793c5af048&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fphotoblog%2F1206%2F21%2Fc2%2F12078509_12078509_1340246370201.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534943746996&di=ad157c6f6cf0559b272718793c5af048&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fphotoblog%2F1206%2F21%2Fc2%2F12078509_12078509_1340246370201.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534943746996&di=ad157c6f6cf0559b272718793c5af048&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fphotoblog%2F1206%2F21%2Fc2%2F12078509_12078509_1340246370201.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534943746996&di=ad157c6f6cf0559b272718793c5af048&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fphotoblog%2F1206%2F21%2Fc2%2F12078509_12078509_1340246370201.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534943746996&di=ad157c6f6cf0559b272718793c5af048&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fphotoblog%2F1206%2F21%2Fc2%2F12078509_12078509_1340246370201.jpg",
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534943746996&di=ad157c6f6cf0559b272718793c5af048&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fphotoblog%2F1206%2F21%2Fc2%2F12078509_12078509_1340246370201.jpg"
]

class CustomVew extends Component {
  constructor(props) {
    super(props);
    this.state = {
    };
  }
  render() {
    return (<img src={`${RNFS.MainBundlePath}/default_header.png`}></img>)
  }
}

export default class TestRNIMUI extends Component {
  constructor(props) {
    super(props);
    let initHeight;
    if (Platform.OS === "ios") {
      initHeight = 46
    } else {
      initHeight = 100
    }
    this.state = {
      inputLayoutHeight: initHeight,
      messageListLayout: { flex: 1, width: window.width, margin: 0 },
      inputViewLayout: { width: window.width, height: initHeight, },
      isAllowPullToRefresh: true,
      navigationBar: {},
    }
    

    this.updateLayout = this.updateLayout.bind(this);
    this.onMsgClick = this.onMsgClick.bind(this);
    this.messageListDidLoadEvent = this.messageListDidLoadEvent.bind(this);
  }

  componentDidMount() {
    /**
     * Android only
     * Must set menu height once, the height should be equals with the soft keyboard height so that the widget won't flash.
     * 在别的界面计算一次软键盘的高度，然后初始化一次菜单栏高度，如果用户唤起了软键盘，则之后会自动计算高度。
     */
    if (Platform.OS === "android") {
      this.refs["ChatInput"].setMenuContainerHeight(316)
    }
    this.resetMenu()
    AuroraIController.addMessageListDidLoadListener(this.messageListDidLoadEvent);
  }

  messageListDidLoadEvent() {
    this.getHistoryMessage()
  }
  
  getHistoryMessage() {
    var messages = []
    for (var index in imageUrlArray) {
      var message = constructNormalMessage()
      message.fromUser.avatarUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534926548887&di=f107f4f8bd50fada6c5770ef27535277&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F11%2F67%2F23%2F69i58PICP37.jpg",//1
      message.msgType = 'image'
      message.mediaPath = imageUrlArray[index]
      message.contentSize = { 'height': 100, 'width': 200 }
      message.extras = { "extras": "fdfsf" }
      messages.push(message)
      // AuroraIController.appendMessages([message])
      // AuroraIController.scrollToBottom(true)
    }
    AuroraIController.appendMessages(messages)
    AuroraIController.scrollToBottom(true)

    // for (var i = 0; i < 10; i++) {
    //   var message = constructNormalMessage()
    //   message.msgType = 'custom'

    //   if (Platform.OS === "ios") {
    //     message.content = `
    //     <h5>This is a custom message. </h5>
    //     <img src="file://${RNFS.MainBundlePath}/default_header.png"/>
    //     `
    //   } else {
    //     message.content = '<body bgcolor="#ff3399"><h5>This is a custom message. </h5>\
    //     <img src="/storage/emulated/0/XhsEmoticonsKeyboard/Emoticons/wxemoticons/icon_040_cover.png"></img></body>'
    //   }

    //   var eventMessage = constructNormalMessage()
    //   eventMessage.msgType = "event"
    //   eventMessage.text = 'fsadfad'

    //   message.contentSize = { 'height': 100, 'width': 200 }
    //   message.extras = { "extras": "fdfsf" }
    //   AuroraIController.appendMessages([message, eventMessage])
    //   AuroraIController.scrollToBottom(true)
    // }
  }

  onInputViewSizeChange = (size) => {
    console.log("onInputViewSizeChange height: " + size.height + " width: " + size.width)
    if (this.state.inputLayoutHeight != size.height) {
      this.setState({
        inputLayoutHeight: size.height,
        inputViewLayout: { width: window.width, height: size.height },
        messageListLayout: { flex: 1, width: window.width, margin: 0 }
      })
    }
  }

  componentWillUnmount() {
    AuroraIController.removeMessageListDidLoadListener(this.messageListDidLoadEvent)
  }

  resetMenu() {
    if (Platform.OS === "android") {
      this.refs["ChatInput"].showMenu(false)
      this.setState({
        messageListLayout: { flex: 1, width: window.width, margin: 0 },
        navigationBar: { height: 64, justifyContent: 'center' },
      })
      this.forceUpdate();
    } else {
      AuroraIController.hidenFeatureView(true)
    }
  }

  /**
   * Android need this event to invoke onSizeChanged 
   */
  onTouchEditText = () => {
    this.refs["ChatInput"].showMenu(false)
  }

  onFullScreen = () => {
    console.log("on full screen")
    this.setState({
      messageListLayout: { flex: 0, width: 0, height: 0 },
      inputViewLayout: { flex: 1, width: window.width, height: window.height },
      navigationBar: { height: 0 }
    })
  }

  onRecoverScreen = () => {
    // this.setState({
    //   inputLayoutHeight: 100,
    //   messageListLayout: { flex: 1, width: window.width, margin: 0 },
    //   inputViewLayout: { flex: 0, width: window.width, height: 100 },
    //   navigationBar: { height: 64, justifyContent: 'center' }
    // })
  }

  onAvatarClick = (message) => {
    Alert.alert()
    AuroraIController.removeMessage(message.msgId)
  }

  onMsgClick(message) {
    console.log(message)
    Alert.alert("message", JSON.stringify(message))
  }

  onMsgLongClick = (message) => {
    Alert.alert('message bubble on long press', 'message bubble on long press')
  }

  onStatusViewClick = (message) => {
    message.status = 'send_succeed'
    AuroraIController.updateMessage(message)
  }

  onBeginDragMessageList = () => {
    this.resetMenu()
    AuroraIController.hidenFeatureView(true)
  }

  onTouchMsgList = () => {
    AuroraIController.hidenFeatureView(true)
  }

  onPullToRefresh = () => {
    console.log("on pull to refresh")
    var messages = []
    for (var i = 0; i < 14; i++) {
      var message = constructNormalMessage()
      // if (index%2 == 0) {
      message.msgType = "text"
      message.text = "" + i
      // }

      if (i % 3 == 0) {
        message.msgType = "video"
        message.text = "" + i
        message.mediaPath = "/storage/emulated/0/ScreenRecorder/screenrecorder.20180323101705.mp4"
        message.duration = 12
      }
      messages.push(message)
    }
    AuroraIController.insertMessagesToTop(messages)
    if (Platform.OS === 'android') {
      this.refs["MessageList"].refreshComplete()
    }

  }

  onSendText = (text) => {
    var message = constructNormalMessage()
    var evenmessage = constructNormalMessage()

    message.msgType = 'text'
    message.text = text

    AuroraIController.appendMessages([message])
  }

  onTakePicture = (media) => {
    console.log("media " + JSON.stringify(media))
    var message = constructNormalMessage()
    message.msgType = 'image'
    message.mediaPath = media.mediaPath
    AuroraIController.appendMessages([message])
    this.resetMenu()
    AuroraIController.scrollToBottom(true)
  }

  onStartRecordVoice = (e) => {
    console.log("on start record voice")
  }

  onFinishRecordVoice = (mediaPath, duration) => {
    var message = constructNormalMessage()
    message.msgType = "voice"
    message.mediaPath = mediaPath
    message.timeString = "safsdfa"
    message.duration = duration
    AuroraIController.appendMessages([message])
    console.log("on finish record voice")
  }

  onCancelRecordVoice = () => {
    console.log("on cancel record voice")
  }

  onStartRecordVideo = () => {
    console.log("on start record video")
  }

  onFinishRecordVideo = (video) => {
    // var message = constructNormalMessage()

    // message.msgType = "video"
    // message.mediaPath = video.mediaPath
    // message.duration = video.duration
    // AuroraIController.appendMessages([message])
  }

  onSendGalleryFiles = (mediaFiles) => {
    /**
     * WARN: This callback will return original image, 
     * if insert it directly will high memory usage and blocking UI。
     * You should crop the picture before insert to messageList。
     * 
     * WARN: 这里返回的是原图，直接插入大会话列表会很大且耗内存.
     * 应该做裁剪操作后再插入到 messageListView 中，
     * 一般的 IM SDK 会提供裁剪操作，或者开发者手动进行裁剪。
     * 
     * 代码用例不做裁剪操作。
     */
    Alert.alert('fas', JSON.stringify(mediaFiles))
    for (index in mediaFiles) {
      var message = constructNormalMessage()
      if (mediaFiles[index].mediaType == "image") {
        message.msgType = "image"
      } else {
        message.msgType = "video"
        message.duration = mediaFiles[index].duration
      }

      message.mediaPath = mediaFiles[index].mediaPath
      message.timeString = "8:00"
      message.status = "send_going"
      AuroraIController.appendMessages([message])
      AuroraIController.scrollToBottom(true)
    }

    this.resetMenu()
  }

  onSwitchToMicrophoneMode = () => {
    AuroraIController.scrollToBottom(true)
  }

  onSwitchToEmojiMode = () => {
    AuroraIController.scrollToBottom(true)
  }
  onSwitchToGalleryMode = () => {
    AuroraIController.scrollToBottom(true)
  }

  onSwitchToCameraMode = () => {
    AuroraIController.scrollToBottom(true)
  }

  onShowKeyboard = (keyboard_height) => {
  }

  updateLayout(layout) {
    this.setState({ inputViewLayout: layout })
  }

  onInitPress() {
    console.log('on click init push ');
    this.updateAction();
  }

  onClickSelectAlbum = () => {
    console.log("on click select album")
  }

  onCloseCamera = () => {
    console.log("On close camera event")
    this.setState({
      inputLayoutHeight: 100,
      messageListLayout: { flex: 1, width: window.width, margin: 0 },
      inputViewLayout: { flex: 0, width: window.width, height: 100 },
      navigationBar: { height: 64, justifyContent: 'center' }
    })
  }

  /**
   * Switch to record video mode or not
   */
  switchCameraMode = (isRecordVideoMode) => {
    console.log("Switching camera mode: isRecordVideoMode: " + isRecordVideoMode)
    // If record video mode, then set to full screen.
    if (isRecordVideoMode) {
      this.setState({
        messageListLayout: { flex: 0, width: 0, height: 0 },
        inputViewLayout: { flex: 1, width: window.width, height: window.height },
        navigationBar: { height: 0 }
      })
    } 
  }

  render() {
    return (
      <View style={styles.container}>
        <View style={this.state.navigationBar}
          ref="NavigatorView">
          <Button
            style={styles.sendCustomBtn}
            title="Custom Message"
            onPress={() => {
              if (Platform.OS === 'ios') {
                var message = constructNormalMessage()
                message.msgType = 'custom'
                message.content = `
                <h5>This is a custom message. </h5>
                <img src="file://${RNFS.MainBundlePath}/default_header.png"/>
                `
                console.log(message.content)
                message.contentSize = { 'height': 100, 'width': 200 }
                message.extras = { "extras": "fdfsf" }
                AuroraIController.appendMessages([message])
                AuroraIController.scrollToBottom(true)
              } else {
                var message = constructNormalMessage()
                message.msgType = "custom"
                message.msgId = "10"
                message.status = "send_going"
                message.isOutgoing = true
                message.content = `
                <body bgcolor="#ff3399">
                  <h5>This is a custom message. </h5>
                  <img src="/storage/emulated/0/XhsEmoticonsKeyboard/Emoticons/wxemoticons/icon_040_cover.png"></img>
                </body>`
                message.contentSize = { 'height': 100, 'width': 200 }
                message.extras = { "extras": "fdfsf" }
                var user = {
                  userId: "1",
                  displayName: "",
                  avatarPath: ""
                }
                user.displayName = "0001"
                user.avatarPath = "ironman"
                message.fromUser = user
                AuroraIController.appendMessages([message]);
              }
            }}>
          </Button>
        </View>
        <MessageListView style={this.state.messageListLayout}
          ref="MessageList"
          isAllowPullToRefresh={true}
          onAvatarClick={this.onAvatarClick}
          onMsgClick={this.onMsgClick}
          onStatusViewClick={this.onStatusViewClick}
          onTouchMsgList={this.onTouchMsgList}
          onTapMessageCell={this.onTapMessageCell}
          onBeginDragMessageList={this.onBeginDragMessageList}
          onPullToRefresh={this.onPullToRefresh}
          avatarSize={{ width: 50, height: 50 }}
          avatarCornerRadius={25}
          messageListBackgroundColor={"#f3f3f3"}
          sendBubbleTextSize={18}
          sendBubbleTextColor={"#000000"}
          sendBubblePadding={{ left: 10, top: 10, right: 15, bottom: 10 }}
          datePadding={{ left: 5, top: 5, right: 5, bottom: 5 }}
          dateBackgroundColor={"#F3F3F3"}
          photoMessageRadius={5}
          maxBubbleWidth={0.7}
          videoDurationTextColor={"#ffffff"}
        />
        <InputView style={this.state.inputViewLayout}
          ref="ChatInput"
          onSendText={this.onSendText}
          onTakePicture={this.onTakePicture}
          onStartRecordVoice={this.onStartRecordVoice}
          onFinishRecordVoice={this.onFinishRecordVoice}
          onCancelRecordVoice={this.onCancelRecordVoice}
          onStartRecordVideo={this.onStartRecordVideo}
          onFinishRecordVideo={this.onFinishRecordVideo}
          onSendGalleryFiles={this.onSendGalleryFiles}
          onSwitchToEmojiMode={this.onSwitchToEmojiMode}
          onSwitchToMicrophoneMode={this.onSwitchToMicrophoneMode}
          onSwitchToGalleryMode={this.onSwitchToGalleryMode}
          onSwitchToCameraMode={this.onSwitchToCameraMode}
          onShowKeyboard={this.onShowKeyboard}
          onTouchEditText={this.onTouchEditText}
          onFullScreen={this.onFullScreen}
          onRecoverScreen={this.onRecoverScreen}
          onSizeChange={this.onInputViewSizeChange}
          closeCamera={this.onCloseCamera}
          switchCameraMode={this.switchCameraMode}
          showSelectAlbumBtn={true}
          showRecordVideoBtn={false}
          onClickSelectAlbum={this.onClickSelectAlbum}
          inputPadding={{ left: 30, top: 10, right: 10, bottom: 10 }}
          galleryScale={0.6}//default = 0.5
          compressionQuality={0.6}
          customLayoutItems={{
            left: [],
            right: ['send','emoji']
            // bottom: ['voice','gallery','emoji','camera']
          }}
        />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  sendCustomBtn: {

  },
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  inputView: {
    backgroundColor: 'green',
    width: window.width,
    height: 100,
  },
  btnStyle: {
    marginTop: 10,
    borderWidth: 1,
    borderColor: '#3e83d7',
    borderRadius: 8,
    backgroundColor: '#3e83d7'
  }
});

