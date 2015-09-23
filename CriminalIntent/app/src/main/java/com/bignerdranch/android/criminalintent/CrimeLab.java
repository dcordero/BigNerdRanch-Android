package com.bignerdranch.android.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.bignerdranch.android.criminalintent.database.CrimeBaseHelper;
import com.bignerdranch.android.criminalintent.database.CrimeCursorWrapper;
import com.bignerdranch.android.criminalintent.database.CrimeDbSchema;
import com.bignerdranch.android.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {

    private static CrimeLab sCrimeLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    public void removeCrime(Crime crime) {
        ContentValues contentValues = getContentValues(crime);
        mDatabase.delete(CrimeTable.NAME,
                CrimeTable.Cols.UUID + " = ?", new String[]{crime.getId().toString()});
    }

    public void addCrime(Crime crime) {
        ContentValues contentValues = getContentValues(crime);
        mDatabase.insert(CrimeTable.NAME, null, contentValues);
    }

    public void updateCrime(Crime crime) {
        ContentValues contentValues = getContentValues(crime);

        mDatabase.update(CrimeTable.NAME,
                contentValues,
                CrimeTable.Cols.UUID + " = ?", new String[]{crime.getId().toString()});
    }

    private CrimeLab(Context context) {
        mContext = context;
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
    }

    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper crimeCursorWrapper = queryCrimes(null, null);
        try {
            crimeCursorWrapper.moveToFirst();
            while (!crimeCursorWrapper.isAfterLast()) {
                crimes.add(crimeCursorWrapper.getCrime());
                crimeCursorWrapper.moveToNext();
            }
        }
        finally {
            crimeCursorWrapper.close();
        }
        return crimes;
    }

    public File getPhotoFile(Crime crime) {
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFilesDir == null) {
            return null;
        }
        return new File(externalFilesDir, crime.getPhotoFileName());
    }

    public Crime getCrime(UUID id) {
        CrimeCursorWrapper crimeCursorWrapper = queryCrimes(
                CrimeTable.Cols.UUID + " = ?",
                new String[] { id.toString() });

        try {
            if (crimeCursorWrapper.getCount() == 0) {
                return null;
            }

            crimeCursorWrapper.moveToFirst();
            return crimeCursorWrapper.getCrime();
        }
        finally {
            crimeCursorWrapper.close();
        }
    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(CrimeTable.Cols.UUID, crime.getId().toString());
        contentValues.put(CrimeTable.Cols.TITLE, crime.getTitle());
        contentValues.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        contentValues.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        contentValues.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());

        return contentValues;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                            CrimeTable.NAME,
                            null, // Columns, null selects all the columns
                            whereClause,
                            whereArgs,
                            null, // groupby
                            null, // having
                            null  // orderBy
        );
        return new CrimeCursorWrapper(cursor);
    }
}
