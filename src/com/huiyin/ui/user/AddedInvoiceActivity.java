package com.huiyin.ui.user;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.InvoiceAddBean;
import com.huiyin.ui.common.CommonConfrimCancelDialog;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.imageupload.BitmapUtils;
import com.huiyin.utils.imageupload.ImageFolder;
import com.huiyin.utils.imageupload.ImageUpload;
import com.huiyin.wight.MyGridView;
import com.huiyin.wight.MyText;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.orhanobut.logger.Logger;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddedInvoiceActivity extends BaseActivity implements
		View.OnClickListener {
	private Context mContext = AddedInvoiceActivity.this;
	private android.widget.EditText invcompany;
	private android.widget.EditText invidentify;
	private android.widget.EditText invregistaddr;
	private android.widget.EditText invphone;
	private android.widget.EditText invbank;
	private android.widget.EditText invaccount;
	private MyGridView uploadimage;
	private android.widget.LinearLayout addedinvlayout1;
	private String TAG = "AddedInvoiceActivity";
	private String capturePath;
	public static final int CAMER_CODE = 007;
	public static final int LOCAL_CODE = 006;
	List<Bitmap> images = new ArrayList<Bitmap>();
	ArrayList<String> imgPathlist = new ArrayList<String>();
	private GridAdapter myGridAdapter;
	private String webImage;
	private String company_name;
	private String identification_number;
	private String registered_address;
	private String registered_phone;
	private String bank;
	private String account;
	private EditText inv_receiver;
	private EditText inv_receiver_phone;
	private EditText inv_receiver_addr;
	private String collector_name;
	private String collector_phone;
	private String collector_address;
    private String id="0";
    private View left_ib;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_added_invoice);
		setTitle("增票资质");
		init();
	}

	private void init() {
		images.add(BitmapFactory.decodeResource(getResources(), R.drawable.tj2));
		myGridAdapter = new GridAdapter(this);
		uploadimage.setAdapter(myGridAdapter);
		getAddedInvoice();
	}

	@Override
	protected void onFindViews() {
        left_ib=findViewById(R.id.left_ib);
        TextView right_ib = (TextView) findViewById(R.id.right_ib);
		right_ib.setOnClickListener(this);
		this.addedinvlayout1 = (LinearLayout) findViewById(R.id.added_inv_layout_1);
		this.uploadimage = (MyGridView) findViewById(R.id.upload_image_grid);
		this.invaccount = (EditText) findViewById(R.id.inv_account);
		this.invbank = (EditText) findViewById(R.id.inv_bank);
		this.invphone = (EditText) findViewById(R.id.inv_phone);
		this.invregistaddr = (EditText) findViewById(R.id.inv_regist_addr);
		this.invidentify = (EditText) findViewById(R.id.inv_identify);
		this.invcompany = (EditText) findViewById(R.id.inv_company);
		this.inv_receiver = (EditText) findViewById(R.id.inv_receiver);
		this.inv_receiver_phone = (EditText) findViewById(R.id.inv_receiver_phone);
		this.inv_receiver_addr = (EditText) findViewById(R.id.inv_receiver_addr);
	}

	@Override
	protected void onSetListener() {
		super.onSetListener();
		uploadimage
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView,
                                            View view, int position, long l) {
                        if (position == images.size() - 1) {
                            if (images.size() >= 2) {
                                UIHelper.showToast("最多只能上传一张图片");
                            } else {
                                selectPhotoWay();
                            }
                        }
                    }
                });
        left_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_added_invoice, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.right_ib:
			UpLoadImage();
			break;
		}
	}

	private void selectPhotoWay() {
		Intent i = new Intent(mContext, CommonConfrimCancelDialog.class);
		i.putExtra(CommonConfrimCancelDialog.TASK,
				CommonConfrimCancelDialog.TASK_CHOICE_UPLOAD_TYPE);
		this.startActivityForResult(i,
				CommonConfrimCancelDialog.CHOICE_UPLOAD_TYPE_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CommonConfrimCancelDialog.CHOICE_UPLOAD_TYPE_CODE
				&& resultCode == RESULT_OK) {
			/* 选择图片来源 */
			String uploadType = data
					.getStringExtra(CommonConfrimCancelDialog.TAG);
			if (uploadType.equals(CommonConfrimCancelDialog.UPLOAD_TYPE_CAMER)) {
				/* 相机 */
				callCamerImage();
			} else if (uploadType
					.equals(CommonConfrimCancelDialog.UPLOAD_TYPE_LOCAL)) {
				/* 图库 */
				callLocalImage();
			}
		} else if ((requestCode == CAMER_CODE || requestCode == LOCAL_CODE)) {
			if (resultCode == RESULT_OK) {
				/* 相机或者图库返回OK */
				Log.i(TAG, "获取图片返回成功");
				dealWithImage(data);
			} else {
				Log.e(TAG, "获取图片失败");
			}
		}
	}

	private void callCamerImage() {
		Log.i(TAG, "启动照相机");
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			Intent captureIntent = new Intent(
					"android.media.action.IMAGE_CAPTURE");
			String out_file_path;
			out_file_path = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/huiyin";
			File dir = new File(out_file_path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			capturePath = ImageFolder.getTempImageName().getPath();
			captureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(capturePath)));
			startActivityForResult(captureIntent, CAMER_CODE);
		}
	}

	private void callLocalImage() {
		Log.i(TAG, "打开图库");
		Intent localIntent = new Intent(Intent.ACTION_PICK);
		localIntent.setType("image/*");
		startActivityForResult(localIntent, LOCAL_CODE);
	}

	private void dealWithImage(Intent data) {
		Bitmap curImagePic;
		if (data != null && data.getData() != null) {
			Uri selectedImage = data.getData();
			String[] filePathColumns = { MediaStore.Images.Media.DATA };
			Cursor c = this.getContentResolver().query(selectedImage,
					filePathColumns, null, null, null);
			c.moveToFirst();
			int columnIndex = c.getColumnIndex(filePathColumns[0]);
			String picturePath = c.getString(columnIndex);
			c.close();
			curImagePic = BitmapUtils.showimageFull(picturePath, 100, 100);
			if (null == curImagePic) {
				return;
			}
			// TODO 图库返回的照片
			images.add(0, curImagePic);
			imgPathlist.add(0, picturePath);
			myGridAdapter.notifyDataSetChanged();
		} else {
			File f = new File(capturePath);
			if (!f.exists()) {
				return;
			} else {
				curImagePic = BitmapUtils.showimageFull(capturePath, 100, 100);
				if (null == curImagePic) {
					return;
				}
				// TODO 拍照返回的照片
				images.add(0, curImagePic);
				imgPathlist.add(0, capturePath);
				myGridAdapter.notifyDataSetChanged();
			}
		}
	}

	public class GridAdapter extends BaseAdapter {

		private LayoutInflater inflater;

		public GridAdapter(Context context) {
			super();
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return images == null ? 0 : images.size();
		}

		@Override
		public Object getItem(int position) {
			return images.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.add_iamge_item, null);
				viewHolder = new ViewHolder();
				viewHolder.image = (ImageView) convertView
						.findViewById(R.id.iv_add_image_item);
				viewHolder.img_delete = (ImageView) convertView
						.findViewById(R.id.iv_delete_jianhao);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if (position == images.size() - 1) {
				viewHolder.img_delete.setVisibility(View.GONE);
			} else {
				viewHolder.img_delete.setVisibility(View.VISIBLE);
				viewHolder.img_delete
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								images.remove(position);
								imgPathlist.remove(position);
								notifyDataSetChanged();
							}
						});
			}
			viewHolder.image.setImageBitmap(images.get(position));
			return convertView;
		}
	}

	class ViewHolder {
		public ImageView image;
		public ImageView img_delete;
	}

	public void UpLoadImage() {
		if (checkInfo()) {
			if (images.size() > 1) {
				for (int i = 0; i < imgPathlist.size(); i++) {
					if (!isSuffxOk(imgPathlist.get(i))) {
						UIHelper.showToast("文件格式不符合要求");
						return;
					}
					if (BitmapUtils.getBitmapSize(images.get(i)) > 4 * 1024 * 1024) {
						UIHelper.showToast("文件大小不符合要求");
						return;
					}
				}
				new ImageUpload(mContext, imgPathlist,
						new ImageUpload.UpLoadImageListener() {
							@Override
							public void UpLoadSuccess(
									ArrayList<String> netimageurls) {
								webImage = "";
								if (netimageurls != null
										&& netimageurls.size() > 0) {
									for (int i = 0; i < netimageurls.size(); i++) {
										webImage += netimageurls.get(i);
										webImage += ",";
									}
									webImage = webImage.substring(0,
											webImage.length() - 1);
									postAddedInvInfo();
								}
							}

							@Override
							public void UpLoadFail() {
								UIHelper.showToast("文件上传失败!");
							}
						}).startLoad();
			} else {
				UIHelper.showToast("请选择上传的资质文件!");
			}
		}
	}

	private boolean checkInfo() {
		company_name = invcompany.getText().toString();
		identification_number = invidentify.getText().toString();
		registered_address = invregistaddr.getText().toString();
		registered_phone = invphone.getText().toString();
		bank = invbank.getText().toString();
		account = invaccount.getText().toString();
		collector_name = inv_receiver.getText().toString();
		collector_phone = inv_receiver_phone.getText().toString();
		collector_address = inv_receiver_addr.getText().toString();

		if (TextUtils.isEmpty(company_name)) {
			Toast.makeText(this, "单位名称不能为空！", Toast.LENGTH_LONG).show();
			return false;
		} else if (company_name.length() > 50) {
			Toast.makeText(this, "单位名称长度不能大于50！", Toast.LENGTH_LONG).show();
			return false;
		} else if (TextUtils.isEmpty(identification_number)) {
			Toast.makeText(this, "识别号不能为空！", Toast.LENGTH_LONG).show();
			return false;
		} else if (TextUtils.isEmpty(registered_address)) {
			Toast.makeText(this, "注册地址不能为空！", Toast.LENGTH_LONG).show();
			return false;
		} else if (registered_address.length() > 100) {
			Toast.makeText(this, "注册地址长度不能大于100！", Toast.LENGTH_LONG).show();
			return false;
		} else if (TextUtils.isEmpty(registered_phone)) {
			Toast.makeText(this, "注册电话不能为空！", Toast.LENGTH_LONG).show();
			return false;
		} else if (TextUtils.isEmpty(bank)) {
			Toast.makeText(this, "开户银行不能为空！", Toast.LENGTH_LONG).show();
			return false;
		} else if (bank.length() > 50) {
			Toast.makeText(this, "开户银行长度不能大于50！", Toast.LENGTH_LONG).show();
			return false;
		} else if (TextUtils.isEmpty(account)) {
			Toast.makeText(this, "银行账号不能为空！", Toast.LENGTH_LONG).show();
			return false;
		}
//        else if (TextUtils.isEmpty(collector_name)) {
//			Toast.makeText(this, "收票人姓名不能为空！", Toast.LENGTH_LONG).show();
//			return false;
//		} else if (collector_name.length() > 50) {
//			Toast.makeText(this, "收票人姓名长度不能大于50！", Toast.LENGTH_LONG).show();
//			return false;
//		} else if (TextUtils.isEmpty(collector_phone)) {
//			Toast.makeText(this, "收票人手机不能为空！", Toast.LENGTH_LONG).show();
//			return false;
//		} else if (!Utils.checkPhoneREX(collector_phone)) {
//			Toast.makeText(this, "收票人手机号码格式不正确！", Toast.LENGTH_LONG).show();
//			return false;
//		} else if (TextUtils.isEmpty(collector_address)) {
//			Toast.makeText(this, "收票人地址不能为空！", Toast.LENGTH_LONG).show();
//			return false;
//		} else if (collector_address.length() > 100) {
//			Toast.makeText(this, "收票人地址长度不能大于100！", Toast.LENGTH_LONG).show();
//			return false;
//		}
		return true;

	}

	private void postAddedInvInfo() {
		if (AppContext.userId != null) {
			RequstClient.postAddedInvoiceInfo(AppContext.userId, "1", "1",
					company_name, identification_number, registered_address,
					registered_phone, bank, account, collector_name,
					collector_phone, collector_address, webImage,id,
					new CustomResponseHandler(this) {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								String content) {
							super.onSuccess(statusCode, headers, content);
							LogUtil.i(TAG, "postAddedInvInfo:" + content);
							try {
								JSONObject obj = new JSONObject(content);
								if (!obj.getString("type").equals("1")) {
									String errorMsg = obj.getString("msg");
									Toast.makeText(getBaseContext(), errorMsg,
											Toast.LENGTH_SHORT).show();
									return;
								}
								UIHelper.showToast("增票资质提交成功");
								finish();
							} catch (JsonSyntaxException e) {
								e.printStackTrace();
							} catch (JSONException e) {
								e.printStackTrace();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
		}
	}

	private boolean isSuffxOk(String imageName) {
		if (imageName.endsWith(".jpg")) {
			return true;
		} else if (imageName.endsWith(".JPG")) {
			return true;
		} else if (imageName.endsWith(".gif")) {
			return true;
		} else if (imageName.endsWith(".GIF")) {
			return true;
		} else if (imageName.endsWith(".png")) {
			return true;
		} else if (imageName.endsWith(".PNG")) {
			return true;
		} else if (imageName.endsWith(".jpeg")) {
			return true;
		} else if (imageName.endsWith(".JPEG")) {
			return true;
		} else if (imageName.endsWith(".bmp")) {
			return true;
		} else if (imageName.endsWith(".BMP")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 查询已有的增值税发票
	 */
	private void getAddedInvoice() {
		if (AppContext.userId != null) {
			RequstClient.vat(AppContext.userId,
					new CustomResponseHandler(this) {
						@Override
						public void onSuccess(String content) {
							super.onSuccess(content);
							Logger.json(content);
							JSONObject obj = null;
							try {
								obj = new JSONObject(content);
								if (!obj.getString("type").equals("1")) {
									String errorMsg = obj.getString("msg");
									UIHelper.showToast(errorMsg);
									return;
								}
								if (obj.optString("vat").equals("{}")) {
									LogUtil.d("AddedInvoiceActivity",
											"返回的增票资质信息为空");
									return;
								}
								InvoiceAddBean mAddBean = new Gson().fromJson(
										content, InvoiceAddBean.class);
                                if (mAddBean.getVat()==null)return;
                                id=mAddBean.getVat().getID();
								invcompany.setText(mAddBean.getVat()
										.getCOMPANY_NAME());
								invidentify.setText(mAddBean.getVat()
										.getIDENTIFICATION_NUMBER());
								invregistaddr.setText(mAddBean.getVat()
										.getREGISTERED_ADDRESS());
								invphone.setText(mAddBean.getVat()
										.getREGISTERED_PHONE());
								invbank.setText(mAddBean.getVat().getBANK());
								invaccount.setText(mAddBean.getVat()
										.getACCOUNT());
								inv_receiver.setText(mAddBean.getVat()
										.getCOLLECTOR_NAME());
								inv_receiver_phone.setText(mAddBean.getVat()
										.getCOLLECTOR_PHONE());
								inv_receiver_addr.setText(mAddBean.getVat()
										.getCOLLECTOR_ADDRESS());
                                if (isSuffxOk(mAddBean.getVat().getIMG())) {
                                    dealWithBitmap(mAddBean.getVat().getIMG());
                                }
                            } catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
		}
	}

    private void dealWithBitmap(final String webImage){
        ImageManager.LoadWithoutImageView(webImage, new SimpleImageLoadingListener() {

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    if (loadedImage != null) {
                        File dir = new File(Environment
                                .getExternalStorageDirectory().getPath()
                                + "/huiyinlehu/image");
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        try {
                            String[] str = webImage.split("/");
                            String FileName = str[str.length - 1];
                            File file = new File(Environment
                                    .getExternalStorageDirectory().getPath()
                                    + "/huiyinlehu/image", FileName);
                            BufferedOutputStream bos = new BufferedOutputStream(
                                    new FileOutputStream(file));
                            loadedImage.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                            bos.flush();
                            bos.close();
                            images.add(0, loadedImage);
                            imgPathlist.add(0,  file.getPath());
                            myGridAdapter.notifyDataSetChanged();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
//            for (int i = 0; i < images.size(); i++) {
//                images.get(i).recycle();
//            }
            images.clear();
            images=null;
        }catch (Exception ignore){

        }
    }
}
