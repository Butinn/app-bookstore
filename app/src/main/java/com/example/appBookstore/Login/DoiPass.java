package com.example.appBookstore.Login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appBookstore.AES;
import com.example.appBookstore.LOPDAO.NVDao;
import com.example.appBookstore.LOPPRODUCT.NhanVien;
import com.example.appBookstore.R;

import org.jetbrains.annotations.NotNull;

public class DoiPass extends Fragment {
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.doipass_fragment, null, false);

    }

    NVDao nvdao;
    EditText ed_passnew, ed_passold, ed_passen;
    Button btn_save, btn_clear;
    NhanVien nhanVien;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ed_passold = view.findViewById(R.id.edpassold);
        ed_passnew = view.findViewById(R.id.edPassnew);
        ed_passen = view.findViewById(R.id.edEnterpass);
        btn_clear = view.findViewById(R.id.btn_clear);
        btn_save = view.findViewById(R.id.btn_save);
        nvdao = new NVDao(getActivity());
        String user = getActivity().getIntent().getStringExtra("admintion");
        nhanVien = nvdao.getId(user);
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_passold.setText("");
                ed_passnew.setText("");
                ed_passen.setText("");
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (checkn() > 0) {
                        try {
                            nhanVien.setMaKhau(AES.encrypt(ed_passnew.getText().toString()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        int kq = nvdao.Thaypass(nhanVien);
                        if (kq > 0) {
                            Toast.makeText(getActivity(), "Thay ?????i m???t kh???u th??nh c??ng", Toast.LENGTH_SHORT).show();
                            ed_passold.setText("");
                            ed_passnew.setText("");
                            ed_passen.setText("");
                        }else {
                            Toast.makeText(getActivity(),"Thay ?????i m???t kh???u th???t b???i",Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public int checkn() throws Exception {
        int check = 1;
        if (ed_passold.getText().length() == 0 || ed_passnew.getText().length() == 0 || ed_passen.getText().length() == 0) {
            Toast.makeText(getActivity(), "H??y ??i???n ?????y ????? th??ng tin !", Toast.LENGTH_SHORT).show();
            check = -1;
        } else {
            String passcurren = nhanVien.getMaKhau();
            String passold = AES.encrypt(ed_passold.getText().toString());
            String passnew = AES.encrypt(ed_passnew.getText().toString());
            String passent = AES.encrypt(ed_passen.getText().toString());
            if (!passcurren.equals(passold)) {
                Toast.makeText(getActivity(), "Sai m???t kh???u c??", Toast.LENGTH_SHORT).show();
                return check = -1;
            }
            if (!passnew.equals(passent)) {
                Toast.makeText(getActivity(), "Nh???p l???i m???t kh???u kh??ng gi???ng", Toast.LENGTH_SHORT).show();
                check = -1;
            }
        }
        return check;
    }
}
