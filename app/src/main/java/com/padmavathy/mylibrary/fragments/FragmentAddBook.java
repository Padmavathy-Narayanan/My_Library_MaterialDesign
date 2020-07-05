package com.padmavathy.mylibrary.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.padmavathy.mylibrary.R;
import com.padmavathy.mylibrary.database.DatabaseHelper;
import com.padmavathy.mylibrary.model.Book;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FragmentAddBook extends Fragment {
    Toolbar toolbar;
    EditText et_bookname,et_author,et_isbn,et_marking,et_binding,et_location,et_condition,et_lentto,et_bookprice,et_pricepaid,et_quantity,et_DateLent,et_DateReturned;
    ImageView imgBackPress,saveData;
    Calendar myCalendar;
    String _path = "";
    private static final String IMAGE_DIRECTORY = "/mylibrary";
    private int GALLERY = 1, CAMERA = 2;
    RelativeLayout selectImage;
    ImageView imgBook;
    Book book;
    DatabaseHelper db;
    String marking,binding,condition;
    TextView tv_field_err;
    String currentDateTimeString;
    public FragmentAddBook(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View postView2 = inflater.inflate(R.layout.fragment_add_book, container, false);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_edit_fragment);

        db = new DatabaseHelper(getContext());
        book = new Book();

        et_bookname = (EditText)postView2.findViewById(R.id.title_add);
        et_author = (EditText)postView2.findViewById(R.id.authors_add);
        et_isbn= (EditText)postView2.findViewById(R.id.isbn_add);
        et_marking= (EditText)postView2.findViewById(R.id.spinner_markings_add);
        et_binding= (EditText)postView2.findViewById(R.id.spinner_bindings_add);
        et_location= (EditText)postView2.findViewById(R.id.loc_add);
        et_condition= (EditText)postView2.findViewById(R.id.spinner_add);
        et_lentto= (EditText)postView2.findViewById(R.id.Lent_to_add);
        et_bookprice= (EditText)postView2.findViewById(R.id.price_add);
        et_pricepaid= (EditText)postView2.findViewById(R.id.PricePaid_add);
        et_quantity= (EditText)postView2.findViewById(R.id.Quantity_add);
        et_DateLent = (EditText)postView2.findViewById(R.id.Datelent_add);
        et_DateReturned = (EditText)postView2.findViewById(R.id.Datereturn_add);
        saveData = (ImageView)postView2.findViewById(R.id.saveData);
        imgBackPress = (ImageView)postView2.findViewById(R.id.img_back_edit);
        tv_field_err = (TextView)postView2.findViewById(R.id.tv_field_err_add);

        selectImage = (RelativeLayout)postView2.findViewById(R.id.img_rel_add);
        imgBook = (ImageView)postView2.findViewById(R.id.img_add);

        myCalendar = Calendar.getInstance();
        tv_field_err.setVisibility(View.GONE);

        imgBackPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FragmentBookList NAME = new FragmentBookList();
                fragmentTransaction.replace(R.id.frame1, NAME);
                fragmentTransaction.commit();
            }
        });

        et_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogCondition();
            }
        });

        et_marking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogMarking();
            }
        });

        et_binding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogBinding();
            }
        });


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        et_DateLent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final DatePickerDialog.OnDateSetListener date_1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel_1();
            }

        };

        et_DateReturned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date_1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMultiplePermissions();
                showPictureDialog();
            }
        });

        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String b =  et_bookname.getText().toString();
                String a = et_author.getText().toString();
                String i = et_isbn.getText().toString();
                String m = et_marking.getText().toString();
                String bind = et_binding.getText().toString();
                String con = et_condition.getText().toString();
                String loc = et_location.getText().toString();
                String book_price = et_bookprice.getText().toString();
                String pricepaid = et_pricepaid.getText().toString();
                String lent_price = et_lentto.getText().toString();
                String dateLent = et_DateLent.getText().toString();
                String dateReturned = et_DateReturned.getText().toString();
                String _quantity  = et_quantity.getText().toString();
                currentDateTimeString= java.text.DateFormat.getDateTimeInstance().format(new Date());
                Log.d("Current DateTime",currentDateTimeString);

                if((!(TextUtils.isEmpty(b))) ||
                        (!(TextUtils.isEmpty(a)))) {
                    tv_field_err.setVisibility(View.GONE);
                       /* (!(TextUtils.isEmpty(m))) &&
                        (!(TextUtils.isEmpty(bind))) &&
                        (!(TextUtils.isEmpty(con)))&&
                        (!(TextUtils.isEmpty(loc)))&&
                        (!(TextUtils.isEmpty(book_price)))&&
                        (!(TextUtils.isEmpty(pricepaid)))&&
                        (!(TextUtils.isEmpty(lent_price)))&&
                        (!(TextUtils.isEmpty(dateLent)))&&
                        (!(TextUtils.isEmpty(dateReturned)))){*/
                    book.setBook(b);
                    book.setAuthor(a);
                    book.setIsbn(i);
                    book.setMarking(m);
                    book.setBinding(bind);
                    book.setCondition(con);
                    book.setLocation(loc);
                    book.setBook_price(book_price);
                    book.setPaid_price(pricepaid);
                    book.setLent_price(lent_price);
                    book.setQuantity(_quantity);
                    book.setImagePath(_path);
                    book.setDateLent(dateLent);
                    book.setDateReturned(dateReturned);
                    book.setCurrentTimestamp(currentDateTimeString);

                    db.insertNote(b,a,i,con,m,bind,loc,lent_price,book_price,pricepaid,_quantity,_path,dateLent,dateReturned,currentDateTimeString);
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FragmentBookList NAME = new FragmentBookList();
                    fragmentTransaction.replace(R.id.frame1, NAME);
                    fragmentTransaction.commit();
                }
                else {
                    if(((TextUtils.isEmpty(b))) &&
                            ((TextUtils.isEmpty(a))) &&
                            ((TextUtils.isEmpty(i))) &&
                            ((TextUtils.isEmpty(m))) &&
                            ((TextUtils.isEmpty(bind))) &&
                            ((TextUtils.isEmpty(con)))&&
                            ((TextUtils.isEmpty(loc)))&&
                            ((TextUtils.isEmpty(book_price)))&&
                            ((TextUtils.isEmpty(lent_price)))&&
                            ((TextUtils.isEmpty(dateLent)))&&
                            ((TextUtils.isEmpty(dateReturned)))){
                        tv_field_err.setVisibility(View.VISIBLE);
                        //Toast.makeText(getContext(), "Please fill any one of the fields", Toast.LENGTH_SHORT).show();
                    }
                   /* if(TextUtils.isEmpty(b)){
                        et_bookname.setError("This field should not be empty");
                    }
                    if(TextUtils.isEmpty(a)){
                        et_author.setError("This field should not be empty");
                    }
                    if(TextUtils.isEmpty(i)){
                        et_isbn.setError("This field should not be empty");
                    }
                    if(TextUtils.isEmpty(m)){
                        et_marking.setError("This field should not be empty");
                    }
                    if(TextUtils.isEmpty(bind)){
                        et_binding.setError("This field should not be empty");
                    }
                    if(TextUtils.isEmpty(con)){
                        et_condition.setError("This field should not be empty");
                    }
                    if(TextUtils.isEmpty(loc)){
                        et_location.setError("This field should not be empty");
                    }
                    if(TextUtils.isEmpty(book_price)){
                        et_bookprice.setError("This field should not be empty");
                    }
                    if(TextUtils.isEmpty(lent_price)){
                        et_lentto.setError("This field should not be empty");
                    }
                    if(TextUtils.isEmpty(dateLent)){
                        et_DateLent.setError("This field should not be empty");
                    }
                    if(TextUtils.isEmpty(dateReturned)){
                        et_DateReturned.setError("This field should not be empty");
                    }*/
                }
            }
        });

        return postView2;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void updateLabel_1() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_DateReturned.setText(sdf.format(myCalendar.getTime()));
    }

    private void showAlertDialogMarking() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Select Markings");
        String[] items = {"Date","Underline","Highlighted"};
        int checkedItem = 1;


        alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        marking = "Date";
                        et_marking.setText(marking);
                        dialog.dismiss();
                        //Toast.makeText(getContext(), "Clicked on Date", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        marking = "Underline";
                        et_marking.setText(marking);
                        dialog.dismiss();
                        //Toast.makeText(getContext(), "Clicked on Underline", Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        marking = "Highlighted";
                        et_marking.setText(marking);
                        dialog.dismiss();
                        //Toast.makeText(getContext(), "Clicked on Highlighed", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
        final AlertDialog alert = alertDialog.create();
        //alert.setCanceledOnTouchOutside(false);
        alert.show();

    }

    private void showAlertDialogCondition() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Select Condition");
        String[] items = {"New","Like New","Good","Fair","Poor"};
        int checkedItem = 1;
        alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        condition = "New";
                        et_condition.setText(condition);
                        dialog.dismiss();
                        // Toast.makeText(getContext(), "Clicked on New", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        condition = "Like New";
                        et_condition.setText(condition);
                        dialog.dismiss();
                        //  Toast.makeText(getContext(), "Clicked on Like New", Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        condition = "Good";
                        et_condition.setText(condition);
                        dialog.dismiss();
                        //  Toast.makeText(getContext(), "Clicked on Good", Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        condition = "Fair";
                        et_condition.setText(condition);
                        dialog.dismiss();
                        // Toast.makeText(getContext(), "Clicked on Fair", Toast.LENGTH_LONG).show();
                        break;
                    case 4:
                        condition = " Poor";
                        et_condition.setText(condition);
                        dialog.dismiss();
                        //Toast.makeText(getContext(), "Clicked on Poor", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
        AlertDialog alert = alertDialog.create();
        //alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    private void showAlertDialogBinding(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Select Binding");
        String[] items = {"Pamphlet","Paper Back","Hard Cover","Digital"};
        int checkedItem = 1;
        alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        binding = "Pamphlet";
                        et_binding.setText(binding);
                        dialog.dismiss();
                        //Toast.makeText(getContext(), "Clicked on Pamphlet", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        binding = "Paper Back";
                        et_binding.setText(binding);
                        dialog.dismiss();
                        //Toast.makeText(getContext(), "Clicked on Paper back", Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        binding = "Hard Cover";
                        et_binding.setText(binding);
                        dialog.dismiss();
                        //Toast.makeText(getContext(), "Clicked on Hard Cover", Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        binding = "Digital";
                        et_binding.setText(binding);
                        dialog.dismiss();
                        //Toast.makeText(getContext(), "Clicked on Digital", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
        AlertDialog alert = alertDialog.create();
        //alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_DateLent.setText(sdf.format(myCalendar.getTime()));
    }
    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getContext());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    //Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    //imageview.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            //imageview.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            //Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getContext(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());
            _path = f.getAbsolutePath();
            Log.d("ByteArray",f.getAbsolutePath().toString());
            Log.d("Image_db",_path);
            showImage(_path);

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void showImage(String path) {
        File imgFile = new  File(path);

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imgBook.setImageBitmap(myBitmap);

        }
    }

    private void requestMultiplePermissions() {
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            //Toast.makeText(getActivity(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getActivity(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }


}
