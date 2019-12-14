package sample.AudioProcessors;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public enum AudioCodec { // enum for all audio codes ffmpeg uses

     AV_CODEC_ID_MP2 ( 86016, "mp2"),
     AV_CODEC_ID_MP3 ( 86017 ,"mp3"),
     AV_CODEC_ID_AAC ( 86018, "aac"),
     AV_CODEC_ID_AAC_M4A ( 86018, "m4a"),
     AV_CODEC_ID_AC3 ( 86019, "ac3"),
     AV_CODEC_ID_DTS ( 86020, "dts"),
     AV_CODEC_ID_VORBIS ( 86021, "vbs"),
     AV_CODEC_ID_DVAUDIO ( 86022, "dva"),
     AV_CODEC_ID_WMAV1 ( 86023, "wma"),
     AV_CODEC_ID_WMAV2 ( 86024, "wma"),
     AV_CODEC_ID_MACE3 ( 86025, "mc3"),
     AV_CODEC_ID_MACE6 ( 86026, "mc6"),
     AV_CODEC_ID_VMDAUDIO ( 86027, "vmda"),
     AV_CODEC_ID_FLAC ( 86028, "flac"),
     AV_CODEC_ID_MP3ADU ( 86029, "m3a"),
     AV_CODEC_ID_MP3ON4 ( 86030, "mp3X4"),
     AV_CODEC_ID_SHORTEN ( 86031, "short"),
     AV_CODEC_ID_ALAC ( 86032, "alac"),
     AV_CODEC_ID_WESTWOOD_SND1 ( 86033, "snd1"),
     AV_CODEC_ID_GSM ( 86034, "gsm"),
     AV_CODEC_ID_QDM2 ( 86035, "qdm2"),
     AV_CODEC_ID_COOK ( 86036, "cook"),
     AV_CODEC_ID_TRUESPEECH ( 86037, "ts"),
     AV_CODEC_ID_TTA ( 86038, "tta"),
     AV_CODEC_ID_SMACKAUDIO ( 86039, "sma"),
     AV_CODEC_ID_QCELP ( 86040, "qcelp"),
     AV_CODEC_ID_WAVPACK ( 86041, "wav"),
     AV_CODEC_ID_DSICINAUDIO ( 86042, "aiff"),
     AV_CODEC_ID_IMC ( 86043, "imc"),
     AV_CODEC_ID_MUSEPACK7 ( 86044, "mup7"),
     AV_CODEC_ID_MLP ( 86045, "mlp"),
     AV_CODEC_ID_GSM_MS ( 86046, "gsmms"),
     AV_CODEC_ID_ATRAC3 ( 86047, "atrac3"),
     AV_CODEC_ID_APE ( 86048, "ape"),
     AV_CODEC_ID_NELLYMOSER ( 86049, "nm"),
     AV_CODEC_ID_MUSEPACK8 ( 86050, "mp8"),
     AV_CODEC_ID_SPEEX ( 86051, "speex"),
     AV_CODEC_ID_WMAVOICE ( 86052, "wmav"),
     AV_CODEC_ID_WMAPRO ( 86053, "wma"),
     AV_CODEC_ID_WMALOSSLESS ( 86054, "wmal"),
     AV_CODEC_ID_ATRAC3P ( 86055, "atrac3p"),
     AV_CODEC_ID_EAC3 ( 86056 ,"eac3"),
     AV_CODEC_ID_SIPR ( 86057, "sipr"),
     AV_CODEC_ID_MP1 ( 86058, "mp1"),
     AV_CODEC_ID_TWINVQ ( 86059,"tvq"),
     AV_CODEC_ID_TRUEHD ( 86060, "thd"),
     AV_CODEC_ID_MP4ALS ( 86061,"mp4als"),
     AV_CODEC_ID_ATRAC1 ( 86062, "atrac1"),
     AV_CODEC_ID_BINKAUDIO_RDFT ( 86063, "bkd"),
     AV_CODEC_ID_BINKAUDIO_DCT ( 86064, "bkd"),
     AV_CODEC_ID_AAC_LATM ( 86065, "acc"),
     AV_CODEC_ID_QDMC ( 86066, "qdmc"),
     AV_CODEC_ID_CELT ( 86067, "celt"),
     AV_CODEC_ID_G723_1 ( 86068, "g7321"),
     AV_CODEC_ID_G729 ( 86069, "g729"),
     AV_CODEC_ID_8SVX_EXP ( 86070, "exp"),
     AV_CODEC_ID_8SVX_FIB ( 86071, "fib"),
     AV_CODEC_ID_BMV_AUDIO ( 86072, "bmv"),
     AV_CODEC_ID_RALF ( 86073, "ralf"),
     AV_CODEC_ID_IAC ( 86074, "iac"),
     AV_CODEC_ID_ILBC ( 86075, "ilbc"),
     AV_CODEC_ID_OPUS ( 86076, "opus"),
     AV_CODEC_ID_COMFORT_NOISE ( 86077, "cn"),
     AV_CODEC_ID_TAK ( 86078, "tak"),
     AV_CODEC_ID_METASOUND ( 86079, "metas"),
     AV_CODEC_ID_PAF_AUDIO ( 86080, "pafa"),
     AV_CODEC_ID_ON2AVC ( 86081, "avc2"),
     AV_CODEC_ID_DSS_SP ( 86082, "ddssp"),
     AV_CODEC_ID_CODEC2 ( 86083, "c2"),
     AV_CODEC_ID_FFWAVESYNTH ( 88064, "wav"),
     AV_CODEC_ID_SONIC ( 88065,"sonic"),
     AV_CODEC_ID_SONIC_LS ( 88066, "soncils"),
     AV_CODEC_ID_EVRC ( 88067, "ervc"),
     AV_CODEC_ID_SMV ( 88068, "smw"),
     AV_CODEC_ID_DSD_LSBF ( 88069, "dl"),
     AV_CODEC_ID_DSD_MSBF ( 88070, "dm"),
     AV_CODEC_ID_DSD_LSBF_PLANAR ( 88071, "lP"),
     AV_CODEC_ID_DSD_MSBF_PLANAR ( 88072, "mp"),
     AV_CODEC_ID_4GV ( 88073, "4gv"),
     AV_CODEC_ID_INTERPLAY_ACM ( 88074, "acm"),
     AV_CODEC_ID_XMA1 ( 88075, "xma"),
     AV_CODEC_ID_XMA2 ( 88076, "xma"),
     AV_CODEC_ID_DST ( 88077, "dst"),
     AV_CODEC_ID_ATRAC3AL ( 88078, "atra"),
     AV_CODEC_ID_ATRAC3PAL ( 88079, "atra"),
     AV_CODEC_ID_DOLBY_E ( 88080, "dolbye"),
     AV_CODEC_ID_APTX ( 88081, "aptx"),
     AV_CODEC_ID_APTX_HD ( 88082, "Aptxhd"),
     AV_CODEC_ID_SBC ( 88083, "sbc"),
     AV_CODEC_ID_ATRAC9 ( 88084, "atra9");




   private final  int  codecNumber;
   private final  String extension;

    AudioCodec(int codecNumber, String extension) {
        this.codecNumber = codecNumber;
        this.extension=extension;
    }

    public int getCodecNumber() {
        return codecNumber;
    }

    public String getExtension() {
        return extension;
    }



    public static List<String> getCodecExtensions(){
         List<String> codecExtensions= new ArrayList<>();
        AudioCodec [] codecs=AudioCodec.values();
         int size=codecs.length;
        for(int count=0; count<size; count++){
             codecExtensions.add(codecs[count].getExtension());

        }


         return codecExtensions;




    }


     @Override
     public String toString() {
          String name=super.toString();
          String name2=StringUtils.remove(name, "AV_CODEC_ID_");
          return name2;

     }
}
