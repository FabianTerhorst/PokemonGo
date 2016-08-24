package com.voxelbusters.nativeplugins.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import com.voxelbusters.nativeplugins.defines.CommonDefines;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtility {
    public static final int IMAGE_QUALITY = 100;

    public static String getContentsOfFile(String filePath) {
        IOException e;
        String dataString = null;
        BufferedReader bufferedReader = null;
        try {
            BufferedReader bufferedReader2 = new BufferedReader(new FileReader(filePath));
            if (bufferedReader2 != null) {
                while (true) {
                    String eachLine = bufferedReader2.readLine();
                    if (eachLine == null) {
                        break;
                    }
                    try {
                        dataString = new StringBuilder(String.valueOf(dataString)).append(eachLine).toString();
                    } catch (IOException e2) {
                        e = e2;
                        bufferedReader = bufferedReader2;
                    }
                }
                bufferedReader = bufferedReader2;
            } else {
                bufferedReader = bufferedReader2;
            }
        } catch (IOException e3) {
            e = e3;
            e.printStackTrace();
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
            return dataString;
        }
        if (bufferedReader != null) {
            bufferedReader.close();
        }
        return dataString;
    }

    public static Uri getSavedFileUri(byte[] data, int length, File destinationDir, String destinationFileName, boolean addScheme) {
        return Uri.fromFile(new File(getSavedFile(data, length, destinationDir, destinationFileName, addScheme, true)));
    }

    public static void replaceFile(byte[] data, File destinationDir, String destinationFileName) {
        if (data != null) {
            getSavedFile(data, data.length, destinationDir, destinationFileName, false);
            return;
        }
        File destinationFile = new File(destinationDir, destinationFileName);
        if (destinationFile.exists()) {
            destinationFile.delete();
        }
    }

    public static String getSavedFile(byte[] data, int length, File destinationDir, String destinationFileName, boolean addScheme) {
        return getSavedFile(data, length, destinationDir, destinationFileName, addScheme, true);
    }

    public static String getSavedFile(byte[] data, int length, File destinationDir, String destinationFileName, boolean addScheme, boolean needsGlobalAccess) {
        IOException e;
        FileNotFoundException e2;
        String destPath = null;
        if (data == null || length <= 0) {
            return destPath;
        }
        createDirectoriesIfUnAvailable(destinationDir.getAbsolutePath());
        File destinationFile = new File(destinationDir, destinationFileName);
        if (destinationFile.exists()) {
            destinationFile.delete();
            try {
                destinationFile.createNewFile();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
        if (needsGlobalAccess) {
            destinationFile.setReadable(true, false);
            destinationFile.setWritable(true, false);
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(destinationFile);
            FileOutputStream fileOutputStream;
            try {
                outputStream.write(data);
                outputStream.close();
                destPath = destinationFile.getAbsolutePath();
                fileOutputStream = outputStream;
            } catch (FileNotFoundException e4) {
                e2 = e4;
                fileOutputStream = outputStream;
                e2.printStackTrace();
                return destPath != null ? destPath : destPath;
            } catch (IOException e5) {
                e3 = e5;
                fileOutputStream = outputStream;
                e3.printStackTrace();
                if (destPath != null) {
                }
            }
        } catch (FileNotFoundException e6) {
            e2 = e6;
            e2.printStackTrace();
            if (destPath != null) {
            }
        } catch (IOException e7) {
            e3 = e7;
            e3.printStackTrace();
            if (destPath != null) {
            }
        }
        if (destPath != null && addScheme) {
            return "file://" + destPath;
        }
    }

    public static void createDirectoriesIfUnAvailable(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static ByteArrayOutputStream getBitmapStream(String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(new File(path).getAbsolutePath());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, IMAGE_QUALITY, stream);
        return stream;
    }

    static void createPathIfUnAvailable(File destinationDir, File destinationFile) {
        if (!destinationFile.exists()) {
            try {
                destinationDir.mkdirs();
                destinationFile.createNewFile();
            } catch (IOException e) {
                Debug.error(CommonDefines.FILE_UTILS_TAG, "Creating file failed!");
                e.printStackTrace();
            }
        }
    }

    public static String createFileFromStream(InputStream stream, File destinationDir, String destinationFileName) {
        File destinationFile = new File(destinationDir, destinationFileName);
        createPathIfUnAvailable(destinationDir, destinationFile);
        try {
            OutputStream out = new FileOutputStream(destinationFile);
            byte[] buf = new byte[1024];
            while (true) {
                int length = stream.read(buf);
                if (length <= 0) {
                    break;
                }
                out.write(buf, 0, length);
            }
            out.close();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return destinationFile.getAbsolutePath();
    }

    public static String getScaledImagePathFromBitmap(Bitmap bitmap, File destinationDir, String destinationFileName, float scaleFactor) {
        FileNotFoundException e;
        Throwable th;
        String absoluteDestinationPath = null;
        File destinationImageFile = new File(destinationDir, destinationFileName);
        createPathIfUnAvailable(destinationDir, destinationImageFile);
        OutputStream outStream = null;
        try {
            OutputStream outStream2 = new FileOutputStream(destinationImageFile);
            int width = 0;
            int height = 0;
            if (bitmap != null) {
                try {
                    width = (int) (((float) bitmap.getWidth()) * scaleFactor);
                    height = (int) (((float) bitmap.getHeight()) * scaleFactor);
                } catch (FileNotFoundException e2) {
                    e = e2;
                    outStream = outStream2;
                    try {
                        Debug.error(CommonDefines.FILE_UTILS_TAG, "Error creating scaled bitmap " + e.getMessage());
                        e.printStackTrace();
                        if (outStream != null) {
                            try {
                                outStream.flush();
                                outStream.close();
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                        }
                        return absoluteDestinationPath;
                    } catch (Throwable th2) {
                        th = th2;
                        if (outStream != null) {
                            try {
                                outStream.flush();
                                outStream.close();
                            } catch (IOException e32) {
                                e32.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    outStream = outStream2;
                    if (outStream != null) {
                        outStream.flush();
                        outStream.close();
                    }
                    throw th;
                }
            }
            if (width == 0 || height == 0) {
                Debug.error(CommonDefines.FILE_UTILS_TAG, "Width and height should be greater than zero. Returning null reference");
            } else {
                Bitmap.createScaledBitmap(bitmap, width, height, true).compress(CompressFormat.JPEG, IMAGE_QUALITY, outStream2);
                absoluteDestinationPath = destinationImageFile.getAbsolutePath();
            }
            if (outStream2 != null) {
                try {
                    outStream2.flush();
                    outStream2.close();
                    outStream = outStream2;
                } catch (IOException e322) {
                    e322.printStackTrace();
                }
                return absoluteDestinationPath;
            }
            outStream = outStream2;
        } catch (FileNotFoundException e4) {
            e = e4;
            Debug.error(CommonDefines.FILE_UTILS_TAG, "Error creating scaled bitmap " + e.getMessage());
            e.printStackTrace();
            if (outStream != null) {
                outStream.flush();
                outStream.close();
            }
            return absoluteDestinationPath;
        }
        return absoluteDestinationPath;
    }

    public static String getScaledImagePath(String sourcePath, File destinationDir, String destinationFileName, float scaleFactor, boolean deleteSource) {
        File sourceImageFile = new File(sourcePath);
        String absoluteDestinationPath = getScaledImagePathFromBitmap(BitmapFactory.decodeFile(sourceImageFile.getAbsolutePath()), destinationDir, destinationFileName, scaleFactor);
        if (deleteSource) {
            sourceImageFile.delete();
        }
        return absoluteDestinationPath;
    }

    public static String getFilePathromURI(Context context, Uri uri) {
        return new File(uri.toString()).getAbsolutePath();
    }

    public static String getSavedLocalFileFromUri(Context context, Uri uri, String folderName, String targetFileName) {
        return getSavedFileFromUri(context, uri, context.getDir(folderName, 0), targetFileName);
    }

    public static String getSavedFileFromUri(Context context, Uri uri, File targetDirectory, String targetFileName) {
        FileNotFoundException e;
        IOException e2;
        ByteArrayOutputStream byteStream = null;
        byte[] byteArray = null;
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteStream2 = new ByteArrayOutputStream();
            try {
                byte[] buffer = new byte[1024];
                while (inputStream.read(buffer) != -1) {
                    byteStream2.write(buffer);
                }
                byteStream2.flush();
                byteArray = byteStream2.toByteArray();
                byteStream = byteStream2;
            } catch (FileNotFoundException e3) {
                e = e3;
                byteStream = byteStream2;
            } catch (IOException e4) {
                e2 = e4;
                byteStream = byteStream2;
            }
        } catch (FileNotFoundException e5) {
            e = e5;
            e.printStackTrace();
            if (byteStream != null) {
                try {
                    byteStream.close();
                } catch (IOException e22) {
                    e22.printStackTrace();
                }
            }
            if (byteArray != null) {
                return null;
            }
            return getSavedFile(byteArray, byteArray.length, targetDirectory, targetFileName, true);
        } catch (IOException e6) {
            e22 = e6;
            e22.printStackTrace();
            if (byteStream != null) {
                byteStream.close();
            }
            if (byteArray != null) {
                return getSavedFile(byteArray, byteArray.length, targetDirectory, targetFileName, true);
            }
            return null;
        }
        if (byteStream != null) {
            byteStream.close();
        }
        if (byteArray != null) {
            return getSavedFile(byteArray, byteArray.length, targetDirectory, targetFileName, true);
        }
        return null;
    }

    public static Uri createSharingFileUri(Context context, byte[] byteArray, int byteArrayLength, String dirName, String fileName) {
        boolean hasExternalDir = ApplicationUtility.hasExternalStorageWritable(context);
        String imagePath = getSavedFile(byteArray, byteArrayLength, ApplicationUtility.getExternalTempDirectoryIfExists(context, dirName), fileName, false);
        Debug.log(CommonDefines.SHARING_TAG, "Saving temp at " + imagePath);
        Uri imageUri = null;
        if (!StringUtility.isNullOrEmpty(imagePath)) {
            if (hasExternalDir) {
                imageUri = Uri.fromFile(new File(imagePath));
            } else {
                imageUri = FileProvider.getUriForFile(context, ApplicationUtility.getFileProviderAuthoityName(context), new File(imagePath));
            }
            context.grantUriPermission(ApplicationUtility.getPackageName(context), imageUri, 3);
        }
        return imageUri;
    }
}
