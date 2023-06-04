package hcmute.edu.vn.distinguishflowers;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.mlkit.common.model.LocalModel;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions;

import hcmute.edu.vn.distinguishflowers.helpers.MLImageHelperActivity;

public class FlowerIdentificationActivity extends MLImageHelperActivity {

    private ImageLabeler imageLabeler;
    private int selectModel = 0;
    private int maxModel = 1;
    private Bitmap bitmapSelected = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpModel();
    }

    private void setUpModel(){
        String model;
        switch (selectModel){
            case 1 :
                model = "lite-model_imagenet_mobilenet_v3_large_100_224_classification_5_metadata_1.tflite";
                System.out.println("Model: " + selectModel);
                break;
            default:
                model = "model.tflite";
                System.out.println("Model: " + selectModel);
                break;
        }
        LocalModel localModel = new LocalModel.Builder().setAssetFilePath(model).build();
        CustomImageLabelerOptions options = new CustomImageLabelerOptions.Builder(localModel)
                .setConfidenceThreshold(0.7f)
                .setMaxResultCount(5)
                .build();
        imageLabeler = ImageLabeling.getClient(options);
    }

    @Override
    protected void runDetection(Bitmap bitmap) {
        InputImage inputImage = InputImage.fromBitmap(bitmap, 0);
        imageLabeler.process(inputImage).addOnSuccessListener(imageLabels -> {
            StringBuilder sc = new StringBuilder();
            StringBuilder sn = new StringBuilder();
            float check = 0;
            for (ImageLabel label : imageLabels) {
                if (label.getConfidence() > check) {
                    System.out.println("Label: " + label.getText());
                    check = label.getConfidence();
                    sn.delete(0,sn.length());
                    sc.delete(0,sc.length());
                    sn.append(label.getText());
                    sc.append(label.getConfidence());
                }
            }

            // nếu imageLabel không tồn tại giá trị phù hợp thì duyệt tới model tiếp theo
            if (imageLabels.isEmpty()) {
                if(selectModel < maxModel){
                    selectModel = selectModel + 1;
                    bitmapSelected = bitmap;
                    changeModel();
                } else {
                    selectModel = 0;
                    bitmapSelected = null;
                    getOutputFlowerName().setText("Can't Distinguish");
                    getOutputConfidence().setText("Could not identify!!");
                }
            } else {
                selectModel = 0;
                bitmapSelected = null;
                setUpModel();
                getOutputFlowerName().setText(sn.toString());
                getOutputConfidence().setText("Confidence: " + sc.toString());
            }
        }).addOnFailureListener(e -> {
            e.printStackTrace();
        });
    }

    private void changeModel() {
        setUpModel();
        if (bitmapSelected != null)
            runDetection(bitmapSelected);
    }
}
