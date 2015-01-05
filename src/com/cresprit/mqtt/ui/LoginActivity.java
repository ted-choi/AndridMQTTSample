package com.cresprit.mqtt.ui;

import com.cresprit.mqtt.R;
import com.cresprit.mqtt.manager.UserManager;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements View.OnClickListener, OnCheckedChangeListener{
	final private static String ERROR_401 = "401"; 
	final private static String ERROR_404 = "404";
	final private static String ERROR_406 = "406";
	final private static String ERROR_500 = "500";
	
	private int DIALOG_LOGIN = 0;
	private Button btnLogin;
	private EditText edtId;
	private EditText edtPasswd;
	private CheckBox cbRememberPwd;
	private UserManager userMgr=null;
	private String m_pId;
	private String m_pPasswd;
	private boolean isChecked=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		
		edtId = (EditText)findViewById(R.id.id_edt);
		
		userMgr = UserManager.getInstance(LoginActivity.this);
		userMgr.setDialogUpdateListener(listener);
		
		edtId.setText(userMgr.getUserId());
		edtId.setSelection(edtId.getText().length());
		
		edtPasswd = (EditText)findViewById(R.id.passwd_edt);
		String pwd = userMgr.getPassword();

		cbRememberPwd = (CheckBox)findViewById(R.id.check_passwd);
		cbRememberPwd.setOnCheckedChangeListener(this);	
		
		if(pwd == null || "".equals(pwd))
			edtPasswd.setText("");
		else
		{
			edtPasswd.setText(pwd);
			edtPasswd.setSelected(true);
			edtPasswd.setSelection(edtPasswd.getText().length());
			cbRememberPwd.setChecked(true);
		}
		btnLogin = (Button)findViewById(R.id.loginbtn);
		btnLogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.loginbtn:
			
			m_pId = edtId.getText().toString();
			m_pPasswd = edtPasswd.getText().toString();
			
			if(m_pId.length() == 0)
			{
				Toast.makeText(this, R.string.insert_id, Toast.LENGTH_SHORT).show();
				break;
			}

			if(!m_pId.contains("@")||!m_pId.contains("."))
			{
				Toast.makeText(this, R.string.invaild_email, Toast.LENGTH_SHORT).show();
				break;				
			}
			else
			{
				String[] ext = m_pId.split("@");
				
				if("".equals(ext[0])||"".equals(ext[1]))
				{
					Toast.makeText(this, R.string.invaild_email, Toast.LENGTH_SHORT).show();
					break;					
				}

			}
			
			if(m_pPasswd.length() == 0)
			{
				Toast.makeText(this, R.string.insert_passwd, Toast.LENGTH_SHORT).show();
				break;				
			}
			

			userMgr.setUserId(m_pId);
			userMgr.setPassword(m_pPasswd);
			
			userMgr.doLogin();
			
			break;
		}
	}


	
	
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		
		if(id == DIALOG_LOGIN)
		{
			 ProgressDialog dialog = new ProgressDialog(this);
			 dialog.setTitle(R.string.login);
			 dialog.setMessage(getResources().getString(R.string.noti_wait_moment));
			 dialog.setIndeterminate(true);
			 dialog.setCancelable(false);
			 return dialog;
		}
		
		return super.onCreateDialog(id);
	}	
		
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		finish();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean _isChecked) {
		// TODO Auto-generated method stub
		isChecked = _isChecked;
	}

	
	private IUpdateListener listener = new IUpdateListener()
	{
		@Override
		public void update(int status, String _key) {
			// TODO Auto-generated method stub
			if(status == IUpdateListener.SHOW_DIALOG)
			{
				showDialog(DIALOG_LOGIN);
			}
			else//status == IUpdateListener.REMOVE_DIALOG
			{
				removeDialog(DIALOG_LOGIN);
				if(_key == null)
					Toast.makeText(LoginActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();//네트워크 에러
				else if(ERROR_401.equals(_key))
					Toast.makeText(LoginActivity.this, R.string.error_different_passwd_eachother, Toast.LENGTH_SHORT).show();
				else if(ERROR_404.equals(_key))
					Toast.makeText(LoginActivity.this, R.string.noti_not_find_email, Toast.LENGTH_SHORT).show();				
				else if(ERROR_406.equals(_key))
					Toast.makeText(LoginActivity.this, R.string.noti_relogin_auth_email, Toast.LENGTH_SHORT).show();
				else if(ERROR_500.equals(_key))
					Toast.makeText(LoginActivity.this, R.string.server_error+_key, Toast.LENGTH_SHORT).show();//서버에러
				else
				{
					UserManager.getInstance(LoginActivity.this).setAuthKey(_key);

					Intent intent = new Intent(LoginActivity.this, DeviceListActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivityForResult(intent, 0);
					if(isChecked == false)
						userMgr.setPassword("");

				}
			}
		}
	};
	
}
