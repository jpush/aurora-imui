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
    @IBOutlet weak var mediaView: UIView!

    lazy var playerLayer : AVPlayerLayer = {
        let aLayer = AVPlayerLayer()
        aLayer.frame = self.mediaView.layer.bounds
        aLayer.videoGravity = AVLayerVideoGravityResizeAspectFill
        self.mediaView.layer.addSublayer(aLayer)
        return aLayer
    }()

    var asset : PHAsset?{
        didSet{
            switch asset!.mediaType {
            case .image:
                PHImageManager.default().requestImage(for: asset!, targetSize: self.frame.size, contentMode: PHImageContentMode.default, options: nil, resultHandler: { [weak self] (image, _) in
                    self?.galleryImageView.image = image
                    self?.galleryImageView.isHidden = false
                    self?.mediaView.isHidden = true
                    self?.playerLayer.player = nil
                })
                break
            case .audio, .video:
                galleryImageView.image = nil
                galleryImageView.isHidden = true
                mediaView.isHidden = false
                PHImageManager.default().requestPlayerItem(forVideo: self.asset!, options: nil, resultHandler: { (avPlayerItem, _) in
                    self.contentView.sendSubview(toBack: self.mediaView!)
                        let player = AVPlayer(playerItem: avPlayerItem)
                        self.playerLayer.player?.pause()
                        self.playerLayer.player = player
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
            let transform = CGAffineTransform(scaleX: scale, y: scale)
            self?.galleryImageView.transform = transform
            self?.grayView.transform = transform
            self?.mediaView.transform = transform
        }) {  [weak self] ( completion ) in
            self?.selectImageView.isHidden = !(self?.didSelect)!
            self?.grayView.isHidden = !(self?.didSelect)!
            self?.bringSubview(toFront: (self?.grayView)!)
            self?.bringSubview(toFront: (self?.selectImageView)!)
        }
    }

}
