package com.huiyin.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.dialog.DownloadDialog;
import com.huiyin.dialog.DownloadDialog.MyCancelClickListener;
import com.huiyin.dialog.UpdateConfirmDialog;
import com.huiyin.dialog.UpdateConfirmDialog.DialogClickListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author coolszy
 * @date 2012-4-26
 * @blog http://blog.92coding.com
 */

public class UpdateManager {
	/* 下载中 */
	private static final int DOWNLOAD = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;
	/* 下载保存路径 */
	private String mSavePath;
	private String mAppName;
	/* 记录进度条数量 */
	private int progress;
	/* 是否取消更新 */
	private boolean cancelUpdate = false;

	private Context mContext;
	/* 更新进度条 */
	private ProgressBar mProgress;
	private TextView tv_pro;

	private DownloadDialog downloadProgress;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 正在下载
			case DOWNLOAD:
				if(mNotification==null) break;
				// 更新进度
				RemoteViews contentView = mNotification.contentView;
				contentView.setTextViewText(R.id.rate, progress + "%");
				contentView.setProgressBar(R.id.progress, 100, progress, false);
				if(mNotificationManager!=null) {
					mNotificationManager.notify(NOTIFY_ID, mNotification);
				}
//				String pro = msg.obj.toString();
//				tv_pro.setText("正在下载（" + pro + "）");
//				// 设置进度条位置
//				mProgress.setProgress(progress);
				break;
			case DOWNLOAD_FINISH:
				// 下载完毕后变换通知形式
				mNotification.flags = Notification.FLAG_AUTO_CANCEL;
				mNotification.contentView = null;

				File apkfile = new File(mSavePath, mAppName);
				if (!apkfile.exists()) {
					return;
				}
				// 通过Intent安装APK文件
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");

				PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, i, PendingIntent.FLAG_ONE_SHOT);
				mNotification.setLatestEventInfo(mContext, "下载完成", "文件已下载完毕", contentIntent);
				if(mNotificationManager!=null) {
					mNotificationManager.notify(NOTIFY_ID, mNotification);
				}
				// 安装文件
				installApk();
				break;
			default:
				break;
			}
		};
	};

	private String downUrl;

	public UpdateManager(Context context, String appName, String downUrl) {
		this.mContext = context;
		mAppName = appName;

		this.downUrl = downUrl;
	}

	/**
	 * 检测软件更新
	 */
	public boolean checkUpdate(int serviceCode, String content) {
		if (isUpdate(serviceCode)) {
			// 显示提示对话框
			showNoticeDialog(content);
			return true;
		} else {

			return false;
		}
	}

	/**
	 * 检查软件是否有更新版本
	 *
	 * @return
	 */
	private boolean isUpdate(int serviceCode) {
		// 获取当前软件版本
		int versionCode = getVersionCode(mContext);
		// // 把version.xml放到网络上，然后获取文件信息
		// InputStream inStream = ParseXmlService.class.getClassLoader()
		// .getResourceAsStream("version.xml");
		// // 解析XML文件。 由于XML文件比较小，因此使用DOM方式进行解析
		// ParseXmlService service = new ParseXmlService();
		// try {
		// mHashMap = service.parseXml(inStream);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// if (null != mHashMap) {
		// int serviceCode = Integer.valueOf(mHashMap.get("version"));
		// 版本判断
		if (serviceCode > versionCode) {
			return true;
		}
		// }
		return false;
	}

	/**
	 * 获取软件版本号
	 *
	 * @param context
	 * @return
	 */
	private int getVersionCode(Context context) {
		int versionCode = 0;
		try {
			String pkg = context.getPackageName();
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionCode = context.getPackageManager().getPackageInfo(pkg, 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 显示软件更新对话框
	 */
	private void showNoticeDialog(String content) {
		// 构造对话框
		UpdateConfirmDialog dialog = new UpdateConfirmDialog(mContext);
		dialog.setCustomTitle("发现新版本");
		dialog.setUpdateContent(content);
		dialog.setConfirm("立即下载");
		dialog.setCancel("稍后更新");
		dialog.setCanceledOnTouchOutside(false);
		dialog.setClickListener(new DialogClickListener() {
			@Override
			public void onConfirmClickListener() {
				showNotification();
//				// 显示下载对话框
//				showDownloadDialog();
			}

			@Override
			public void onCancelClickListener() {

			}
		});
		dialog.show();
	}

	private NotificationManager mNotificationManager;
	private Notification mNotification;
	public static final int NOTIFY_ID = 0x49;//十进制的73

	private void showNotification() {
		mNotification = new Notification(R.drawable.ic_launcher, "开始下载", System.currentTimeMillis());
		RemoteViews contentView = new RemoteViews(mContext.getPackageName(), R.layout.download_notification_layout);
		contentView.setTextViewText(R.id.fileName, "huiyinlehu.apk");
		// 指定个性化视图
		mNotification.contentView = contentView;
		// 放置在"正在运行"栏目中
		mNotification.flags = Notification.FLAG_ONGOING_EVENT;
		mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(NOTIFY_ID, mNotification);

		// 现在文件
		downloadApk();
	}
	/**
	 * 显示软件下载对话框
	 */
	private void showDownloadDialog() {
		// 构造软件下载对话框
		DownloadDialog dialog = new DownloadDialog(mContext);
		dialog.setTitle("下载更新");
		mProgress = dialog.getProgressBar();
		tv_pro = dialog.getMessage();

		dialog.setMyClickListener(new MyCancelClickListener() {
			public void onCancelClickListener() {
				cancelUpdate = true;
			}
		});
		dialog.show();

		// 现在文件
		downloadApk();
	}

	/**
	 * 下载apk文件
	 */
	private void downloadApk() {
		// 启动新线程下载软件
		new downloadApkThread().start();
	}

	/**
	 * 下载文件线程
	 *
	 * @author coolszy
	 * @date 2012-4-26
	 * @blog http://blog.92coding.com
	 */
	private class downloadApkThread extends Thread {
		@Override
		public void run() {
			try {
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory() + "/";
					mSavePath = sdpath + "download";
					URL url = new URL(downUrl);
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists()) {
						file.mkdir();
					}

					File apkFile = new File(mSavePath, mAppName);
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					int sumNumread = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do {
						int numread = is.read(buf);
						count += numread;
						sumNumread += numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);

						if(((float) sumNumread / length) * 100>=5) {
							sumNumread = 0;
							Message msg = new Message();
							msg.obj = progress + "%";
							msg.what = DOWNLOAD;
							// 更新进度
							mHandler.sendMessage(msg);
						}

                        if (numread <= 0) {
							// 下载完成
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 点击取消就停止下载.
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// 取消下载对话框显示
			if (downloadProgress != null ) {
				downloadProgress.dismiss();
			}
		}
	}

	/**
	 * 安装APK文件
	 */
	private void installApk() {
		File apkfile = new File(mSavePath, mAppName);
		if (!apkfile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
}