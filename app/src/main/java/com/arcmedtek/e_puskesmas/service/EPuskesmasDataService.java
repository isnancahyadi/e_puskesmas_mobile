package com.arcmedtek.e_puskesmas.service;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.arcmedtek.e_puskesmas.config.SessionManager;
import com.arcmedtek.e_puskesmas.config.SingletonReq;
import com.arcmedtek.e_puskesmas.model.BpjsModel;
import com.arcmedtek.e_puskesmas.model.DoctorScheduleModel;
import com.arcmedtek.e_puskesmas.model.HistoryModel;
import com.arcmedtek.e_puskesmas.model.PatientModel;
import com.arcmedtek.e_puskesmas.model.QueueModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EPuskesmasDataService {
    public static final String IPCONF = "192.168.188.147";
    public static final String BASE_URL = "http://" + IPCONF + "/e-puskesmas/public/";

    public static final String QUERY_FOR_LOGIN = BASE_URL + "restapi/auth/loginprocess";
    public static final String QUERY_FOR_LOGOUT = BASE_URL + "restapi/auth/logoutprocess";

    public static final String QUERY_FOR_CREATE_ACCOUNT = BASE_URL + "restapi/akun";
    public static final String QUERY_FOR_CREATE_DATA_PATIENT = BASE_URL + "restapi/pasien";
    public static final String QUERY_FOR_CREATE_DATA_BPJS = BASE_URL + "restapi/bpjs";

    public static final String QUERY_FOR_GET_DATA_PATIENT = BASE_URL + "restapi/pasien/";
    public static final String QUERY_FOR_GET_DOCTOR_SCHEDULE = BASE_URL + "restapi/jadwaldokter/";
    public static final String QUERY_FOR_GET_PATIENT_QUEUE = BASE_URL + "restapi/antrian/showspecifiedpatient/";
    public static final String QUERY_FOR_GET_PATIENT_BPJS = BASE_URL + "restapi/bpjs/";
    public static final String QUERY_FOR_GET_QUEUE_GENERAL_POLY = BASE_URL + "restapi/poliumum/showcounter";
    public static final String QUERY_FOR_GET_QUEUE_CHILD_POLY = BASE_URL + "restapi/polianak/showcounter";
    public static final String QUERY_FOR_GET_QUEUE_KB_POLY = BASE_URL + "restapi/polikb/showcounter";
    public static final String QUERY_FOR_GET_QUEUE_KIA_POLY = BASE_URL + "restapi/polikia/showcounter";
    public static final String QUERY_FOR_GET_HISTORY_PATIENT = BASE_URL + "restapi/pemeriksaan/";

    public static final String QUERY_FOR_POST_POLY_REGISTRATION = BASE_URL + "restapi/antrian/create";

    Context context;
    SessionManager _sessionManager;
    String _privilege, _user;
    HashMap<String, String> _userKey;

    public EPuskesmasDataService(Context context) {
        this.context = context;

        _sessionManager = new SessionManager(context);
        _userKey = _sessionManager.getUserDetail();
    }

    public interface LoginListener {
        void onResponse(String privilege);
        void onError(String message);
    }

    public void logIn(String username, String password, LoginListener loginListener) {
        StringRequest request = new StringRequest(Request.Method.POST, QUERY_FOR_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                _privilege = "";
                _user = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    _user = jsonObject.getString("username");
                    _privilege = jsonObject.getString("hak_akses");
                    _sessionManager.createSession(_user, _privilege);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                loginListener.onResponse(_privilege);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    loginListener.onError(String.valueOf(error.networkResponse.statusCode));
                } catch (Exception e) {
                    loginListener.onError("Terjadi kesalahan sistem");
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
        SingletonReq.getInstance(context).addToRequestQueue(request);
    }

    public interface LogoutListener {
        void onResponse(String message);

        void onError(String message);
    }

    public void logout(LogoutListener logoutListener) {
        StringRequest request = new StringRequest(Request.Method.GET, QUERY_FOR_LOGOUT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                logoutListener.onResponse("Berhasil logout");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                logoutListener.onError("Gagal logout");
            }
        });
        SingletonReq.getInstance(context).addToRequestQueue(request);
    }

    public interface SignUpListener {
        void onResponse(String message);
        void onError(String message);
    }

    public void signUp(String username, String password, SignUpListener signUpListener) {
        StringRequest request = new StringRequest(Request.Method.POST, QUERY_FOR_CREATE_ACCOUNT, response -> signUpListener.onResponse("Akun berhasil dibuat"), new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    signUpListener.onError(String.valueOf(error.networkResponse.statusCode));
                } catch (Exception e) {
                    signUpListener.onError("Terjadi kesalahan sistem");
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
        SingletonReq.getInstance(context).addToRequestQueue(request);
    }

    public interface SearchPatient {
        void onResponse(ArrayList<PatientModel> dataPatient);
        void onError(String message);
    }

    public void searchPatient(String nik, SearchPatient searchPatient) {
        ArrayList<PatientModel> models = new ArrayList<>();

        StringRequest request = new StringRequest(Request.Method.GET, QUERY_FOR_GET_DATA_PATIENT + nik, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    PatientModel patientModel = new PatientModel();
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);

                        patientModel.setNik(data.getString("nik"));
                        patientModel.setFirstName(data.getString("nama_depan"));
                        patientModel.setMiddleName(data.getString("nama_tengah"));
                        patientModel.setLastName(data.getString("nama_belakang"));
                        patientModel.setGender(data.getString("jenis_kelamin"));
                        patientModel.setBirthPlace(data.getString("tempat_lahir"));
                        patientModel.setBirthDate(data.getString("tanggal_lahir"));
                        patientModel.setAddress(data.getString("alamat"));
                        patientModel.setPhotoKtp(BASE_URL + "img/ktp/" + data.getString("foto_ktp"));

                        models.add(patientModel);
                    }
                    searchPatient.onResponse(models);
                } catch (JSONException e) {
                    searchPatient.onError("Terjadi kesalahan sistem");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                searchPatient.onError("Terjadi kesalahan sistem");
            }
        });
        SingletonReq.getInstance(context).addToRequestQueue(request);
    }

    public interface CreateDataPatient {
        void onResponse(String message);
        void onError(String message);
    }

    public void createDataPatient(String nik, String frontName, String middleName, String lastName, String gender, String birthPlace, String birthDate, String address, String photoKTP, CreateDataPatient createDataPatient) {
        
        StringRequest request = new StringRequest(Request.Method.POST, QUERY_FOR_CREATE_DATA_PATIENT, response -> createDataPatient.onResponse("Data pasien berhasil teregistrasi"), error -> createDataPatient.onError(String.valueOf(error.getMessage()))) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nik", nik);
                params.put("nama_depan", frontName);
                params.put("nama_tengah", middleName);
                params.put("nama_belakang", lastName);
                params.put("jenis_kelamin", gender);
                params.put("tempat_lahir", birthPlace);
                params.put("tanggal_lahir", birthDate);
                params.put("alamat", address);
                params.put("foto_ktp", photoKTP);
                return params;
            }
        };
        SingletonReq.getInstance(context).addToRequestQueue(request);
    }

    public interface GetDataPatient {
        void onResponse(ArrayList<PatientModel> dataPatient);
        void onError(String message);
    }

    public void getDataPatient(GetDataPatient getDataPatient) {
        ArrayList<PatientModel> models = new ArrayList<>();

        StringRequest request = new StringRequest(Request.Method.GET, QUERY_FOR_GET_DATA_PATIENT + _userKey.get(SessionManager.USERNAME), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    PatientModel patientModel = new PatientModel();
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);

                        patientModel.setNik(data.getString("nik"));
                        patientModel.setFirstName(data.getString("nama_depan"));
                        patientModel.setMiddleName(data.getString("nama_tengah"));
                        patientModel.setLastName(data.getString("nama_belakang"));
                        patientModel.setGender(data.getString("jenis_kelamin"));
                        patientModel.setBirthPlace(data.getString("tempat_lahir"));
                        patientModel.setBirthDate(data.getString("tanggal_lahir"));
                        patientModel.setAddress(data.getString("alamat"));
                        patientModel.setPhotoKtp(BASE_URL + "img/ktp/" + data.getString("foto_ktp"));

                        models.add(patientModel);
                    }
                    getDataPatient.onResponse(models);
                } catch (JSONException e) {
                    getDataPatient.onError("Terjadi kesalahan sistem");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getDataPatient.onError("Terjadi kesalahan sistem");
            }
        });
        SingletonReq.getInstance(context).addToRequestQueue(request);
    }

    public interface GetDoctorSchedule {
        void onResponse(ArrayList<DoctorScheduleModel> dataDoctorSchedule);
        void onError(String message);
    }

    public void getDoctorSchedule(String poly, GetDoctorSchedule dataDoctorSchedule) {
        ArrayList<DoctorScheduleModel> models = new ArrayList<>();

        StringRequest request = new StringRequest(Request.Method.GET, QUERY_FOR_GET_DOCTOR_SCHEDULE + poly, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        DoctorScheduleModel doctorScheduleModel = new DoctorScheduleModel();
                        JSONObject data = jsonArray.getJSONObject(i);

                        doctorScheduleModel.setDay(data.getString("hari"));
                        doctorScheduleModel.setFrontDegree(data.getString("gelar_depan"));
                        doctorScheduleModel.setFirstName(data.getString("nama_depan"));
                        doctorScheduleModel.setMiddleName(data.getString("nama_tengah"));
                        doctorScheduleModel.setLastName(data.getString("nama_belakang"));
                        doctorScheduleModel.setLastDegree(data.getString("gelar_belakang"));

                        models.add(doctorScheduleModel);
                    }
                    dataDoctorSchedule.onResponse(models);
                } catch (JSONException e) {
                    dataDoctorSchedule.onError("Terjadi kesalahan sistem");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dataDoctorSchedule.onError("Terjadi kesalahan sistem");
            }
        });
        SingletonReq.getInstance(context).addToRequestQueue(request);
    }

    public interface PostPolyRegistration {
        void onResponse(String message);
        void onError(String message);
    }

    public void postPolyRegistration(String patientType, String poly, String day, String date, PostPolyRegistration postPolyRegistration) {

        StringRequest request = new StringRequest(Request.Method.POST, QUERY_FOR_POST_POLY_REGISTRATION, response -> postPolyRegistration.onResponse("Pasien berhasil terdaftar"), error -> postPolyRegistration.onError(String.valueOf(error.networkResponse.statusCode))) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nik", _userKey.get(SessionManager.USERNAME));
                params.put("jenis_pasien", patientType);
                params.put("poli", poly);
                params.put("hari", day);
                params.put("tanggal", date);
                return params;
            }
        };
        SingletonReq.getInstance(context).addToRequestQueue(request);
    }

    public interface GetPatientQueue {
        void onResponse(ArrayList<QueueModel> dataPatientQueue);
        void onError(String message);
    }

    public void getPatientQueue(GetPatientQueue patientQueue) {
        ArrayList<QueueModel> models = new ArrayList<>();

        StringRequest request = new StringRequest(Request.Method.GET, QUERY_FOR_GET_PATIENT_QUEUE + _userKey.get(SessionManager.USERNAME), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        QueueModel queueModel = new QueueModel();
                        JSONObject data = jsonArray.getJSONObject(i);

                        queueModel.setQueueNum(data.getString("no_antrian"));
                        queueModel.setNik(data.getString("nik"));
                        queueModel.setPasFirstName(data.getString("pas_nama_depan"));
                        queueModel.setPasMiddleName(data.getString("pas_nama_tengah"));
                        queueModel.setPasLastName(data.getString("pas_nama_belakang"));
                        queueModel.setPasGender(data.getString("pas_jenis_kelamin"));
                        queueModel.setPoly(data.getString("nama_poli"));
                        queueModel.setDay(data.getString("hari"));
                        queueModel.setDate(data.getString("tanggal"));
                        queueModel.setPatientType(data.getString("jenis_pasien"));
                        queueModel.setStatus(data.getString("status"));

                        models.add(queueModel);
                    }
                    patientQueue.onResponse(models);
                } catch (JSONException e) {
                    e.printStackTrace();
                    patientQueue.onError("Terjadi kesalahan sistem");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                patientQueue.onError("Terjadi kesalahan sistem");
            }
        });
        SingletonReq.getInstance(context).addToRequestQueue(request);
    }

    public interface GetPatientBpjs {
        void onResponse(ArrayList<BpjsModel> dataPatientBpjs);
        void onError(String message);
    }

    public void getPatientBpjs(GetPatientBpjs dataPatientBpjs) {
        ArrayList<BpjsModel> models = new ArrayList<>();

        StringRequest request = new StringRequest(Request.Method.GET, QUERY_FOR_GET_PATIENT_BPJS + _userKey.get(SessionManager.USERNAME), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        BpjsModel bpjsModel = new BpjsModel();
                        JSONObject data = jsonArray.getJSONObject(i);

                        bpjsModel.setIdBpjs(data.getString("ID_bpjs"));
                        bpjsModel.setNik(data.getString("nik"));
                        bpjsModel.setFaksesLevel(data.getString("tingkat_faskes"));
                        bpjsModel.setFaskesLevelName(data.getString("nama_tingkat_faskes"));
                        bpjsModel.setPhotoBpjs(BASE_URL + "img/bpjs/" + data.getString("foto_bpjs"));

                        models.add(bpjsModel);
                    }
                    dataPatientBpjs.onResponse(models);
                } catch (JSONException e) {
                    dataPatientBpjs.onError("Terjadi kesalahan sistem");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dataPatientBpjs.onError("Terjadi kesalahan sistem");
            }
        });
        SingletonReq.getInstance(context).addToRequestQueue(request);
    }

    public interface CreateDataBpjs {
        void onResponse(String message);
        void onError(String message);
    }

    public void createDataBpjs(String idBpjs, String nik, String faskesLevel, String faskesName, String photoBPJS, CreateDataBpjs createDataBpjs) {

        StringRequest request = new StringRequest(Request.Method.POST, QUERY_FOR_CREATE_DATA_BPJS, response -> createDataBpjs.onResponse("Data pasien berhasil teregistrasi"), error -> createDataBpjs.onError(String.valueOf(error.getLocalizedMessage()))) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ID_bpjs", idBpjs);
                params.put("nik", nik);
                params.put("tingkat_faskes", faskesLevel);
                params.put("nama_tingkat_faskes", faskesName);
                params.put("foto_bpjs", photoBPJS);
                return params;
            }
        };
        SingletonReq.getInstance(context).addToRequestQueue(request);
    }

    public interface GetQueueGeneralPoly {
        void onResponse(String queueNum);
        void onError(String message);
    }

    public void getQueueGeneralPoly(GetQueueGeneralPoly getQueueGeneralPoly) {
        StringRequest request = new StringRequest(Request.Method.GET, QUERY_FOR_GET_QUEUE_GENERAL_POLY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String queueNum = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    queueNum = jsonObject.getString("num_count");
                } catch (JSONException e) {
                    getQueueGeneralPoly.onError("Terjadi kesalahan sistem");
                }
                getQueueGeneralPoly.onResponse(queueNum);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getQueueGeneralPoly.onError("Terjadi kesalahan sistem");
            }
        });
        SingletonReq.getInstance(context).addToRequestQueue(request);
    }

    public interface GetQueueChildPoly {
        void onResponse(String queueNum);
        void onError(String message);
    }

    public void getQueueChildPoly(GetQueueChildPoly getQueueChildPoly) {
        StringRequest request = new StringRequest(Request.Method.GET, QUERY_FOR_GET_QUEUE_CHILD_POLY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String queueNum = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    queueNum = jsonObject.getString("num_count");
                } catch (JSONException e) {
                    getQueueChildPoly.onError("Terjadi kesalahan sistem");
                }
                getQueueChildPoly.onResponse(queueNum);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getQueueChildPoly.onError("Terjadi kesalahan sistem");
            }
        });
        SingletonReq.getInstance(context).addToRequestQueue(request);
    }

    public interface GetQueueKbPoly {
        void onResponse(String queueNum);
        void onError(String message);
    }

    public void getQueueKbPoly(GetQueueKbPoly getQueueKbPoly) {
        StringRequest request = new StringRequest(Request.Method.GET, QUERY_FOR_GET_QUEUE_KB_POLY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String queueNum = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    queueNum = jsonObject.getString("num_count");
                } catch (JSONException e) {
                    getQueueKbPoly.onError("Terjadi kesalahan sistem");
                }
                getQueueKbPoly.onResponse(queueNum);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getQueueKbPoly.onError("Terjadi kesalahan sistem");
            }
        });
        SingletonReq.getInstance(context).addToRequestQueue(request);
    }

    public interface GetQueueKiaPoly {
        void onResponse(String queueNum);
        void onError(String message);
    }

    public void getQueueKiaPoly(GetQueueKiaPoly getQueueKiaPoly) {
        StringRequest request = new StringRequest(Request.Method.GET, QUERY_FOR_GET_QUEUE_KIA_POLY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String queueNum = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    queueNum = jsonObject.getString("num_count");
                } catch (JSONException e) {
                    getQueueKiaPoly.onError("Terjadi kesalahan sistem");
                }
                getQueueKiaPoly.onResponse(queueNum);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getQueueKiaPoly.onError("Terjadi kesalahan sistem");
            }
        });
        SingletonReq.getInstance(context).addToRequestQueue(request);
    }

    public interface GetHistoryPatient {
        void onResponse(ArrayList<HistoryModel> historyPatient);
        void onError(String message);
    }

    public void getHistoryPatient(GetHistoryPatient gethistoryPatient) {
        ArrayList<HistoryModel> models = new ArrayList<>();

        StringRequest request = new StringRequest(Request.Method.GET, QUERY_FOR_GET_HISTORY_PATIENT + _userKey.get(SessionManager.USERNAME), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        HistoryModel historyModel = new HistoryModel();
                        JSONObject data = jsonArray.getJSONObject(i);

                        historyModel.setDay(data.getString("hari"));
                        historyModel.setDate(data.getString("tanggal"));
                        historyModel.setNik(data.getString("nik"));
                        historyModel.setPoly(data.getString("nama_poli"));
                        historyModel.setComplaint(data.getString("keluhan"));
                        historyModel.setDiagnosis(data.getString("diagnosis"));
                        historyModel.setMedicine(data.getString("obat"));
                        historyModel.setNote(data.getString("catatan"));
                        historyModel.setPasFirstName(data.getString("pas_nama_depan"));
                        historyModel.setPasMiddleName(data.getString("pas_nama_tengah"));
                        historyModel.setPasLastName(data.getString("pas_nama_belakang"));
                        historyModel.setDokFrontDegree(data.getString("dok_gelar_depan"));
                        historyModel.setDokFirstName(data.getString("dok_nama_depan"));
                        historyModel.setDokMiddleName(data.getString("dok_nama_tengah"));
                        historyModel.setDokLastName(data.getString("dok_nama_belakang"));
                        historyModel.setDokLastDegree(data.getString("dok_gelar_belakang"));

                        models.add(historyModel);
                    }
                    gethistoryPatient.onResponse(models);
                } catch (JSONException e) {
                    gethistoryPatient.onError("Terjadi kesalahan sistem");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                gethistoryPatient.onError("Terjadi kesalahan sistem");
            }
        });
        SingletonReq.getInstance(context).addToRequestQueue(request);
    }
}
