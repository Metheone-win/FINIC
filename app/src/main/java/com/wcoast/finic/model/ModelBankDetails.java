package com.wcoast.finic.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.wcoast.finic.utility.Constant;

public class ModelBankDetails implements Parcelable {

    @SerializedName(Constant.ACCOUNT_NO)
    private String accountNo;

    @SerializedName(Constant.ACCOUNT_HOLDER_NAME)
    private String accountHolderName;

    @SerializedName(Constant.IFSC_CODE)
    private String ifscCode;

    @SerializedName(Constant.BRANCH_NAME)
    private String branchName;

    @SerializedName(Constant.BANK_NAME)
    private String bankName;

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getBankName() {
        return bankName;
    }

    public ModelBankDetails() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accountNo);
        dest.writeString(this.accountHolderName);
        dest.writeString(this.ifscCode);
        dest.writeString(this.branchName);
        dest.writeString(this.bankName);
    }

    protected ModelBankDetails(Parcel in) {
        this.accountNo = in.readString();
        this.accountHolderName = in.readString();
        this.ifscCode = in.readString();
        this.branchName = in.readString();
        this.bankName = in.readString();
    }

    public static final Creator<ModelBankDetails> CREATOR = new Creator<ModelBankDetails>() {
        @Override
        public ModelBankDetails createFromParcel(Parcel source) {
            return new ModelBankDetails(source);
        }

        @Override
        public ModelBankDetails[] newArray(int size) {
            return new ModelBankDetails[size];
        }
    };
}
