//
//  IMUIGalleryCell.swift
//  IMUIChat
//
//  Created by wuxingchen on 2017/3/14.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit
import Photos

class IMUIGalleryCell: UICollectionViewCell {

    @IBOutlet weak var grayView: UIView!
    @IBOutlet weak var galleryImageView: UIImageView!
    @IBOutlet weak var selectImageView: UIImageView!

    var asset : PHAsset?{
        didSet{
            switch asset!.mediaType {
            case .image:
                PHImageManager.default().requestImage(for: asset!, targetSize: self.frame.size, contentMode: PHImageContentMode.default, options: nil, resultHandler: { [weak self] (image, _) in
                    self?.galleryImageView.image = image
                })
                break
            case .audio, .video:
                PHImageManager.default().requestPlayerItem(forVideo: asset!, options: nil, resultHandler: {[weak self] (avPlayerItem, _) in
                    let player = AVPlayer(playerItem: avPlayerItem)
                    let playerLayer = AVPlayerLayer(player: player)
                    playerLayer.frame = (self?.layer.bounds)!
                    playerLayer.videoGravity = AVLayerVideoGravityResizeAspectFill
                    self?.layer.addSublayer(playerLayer)
                    player.play()
                })
                break
            default:
                break
            }

            animate(duration: 0)

        }
    }

    private var didSelect : Bool{
        get{
            return IMUIGalleryDataManager.selectedAssets.contains(asset!)
        }
    }

    func clicked(){
        if didSelect {
            IMUIGalleryDataManager.selectedAssets = IMUIGalleryDataManager.selectedAssets.filter({$0 != asset!})
        }else{
            IMUIGalleryDataManager.selectedAssets.append(asset!)
        }
        animate(duration: 0.2)
    }

    private func animate(duration:TimeInterval){
        var scale : CGFloat = 1
        if didSelect {
            scale = 0.9
        }else{
            scale = 1/0.9
        }
        UIView.animate(withDuration: duration, animations: { [weak self] in
            self?.galleryImageView.transform = CGAffineTransform(scaleX: scale, y: scale)
            self?.grayView.transform = CGAffineTransform(scaleX: scale, y: scale)
        }) {  [weak self] ( completion ) in
            self?.selectImageView.isHidden = !(self?.didSelect)!
            self?.grayView.isHidden = !(self?.didSelect)!
            self?.bringSubview(toFront: (self?.grayView)!)
            self?.bringSubview(toFront: (self?.selectImageView)!)
        }
    }

}
