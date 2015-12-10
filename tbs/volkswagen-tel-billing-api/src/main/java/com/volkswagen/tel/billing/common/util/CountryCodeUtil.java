package com.volkswagen.tel.billing.common.util;

public class CountryCodeUtil {
	public static String getCountryNameByCountryCode(String countryCode) {
		String rtn = "";

		int ccNumber = Integer.parseInt(countryCode);

		switch (ccNumber) {
		case 244:
			rtn = "Angola";
			break;
		case 93:
			rtn = "Afghanistan";
			break;
		case 355:
			rtn = "Albania";
			break;
		case 213:
			rtn = "Algeria";
			break;
		case 376:
			rtn = "Andorra";
			break;
		case 1264:
			rtn = "Anguilla";
			break;
		case 1268:
			rtn = "Antigua and Barbuda";
			break;
		case 54:
			rtn = "Argentina";
			break;
		case 374:
			rtn = "Armenia";
			break;
		case 247:
			rtn = "Ascension";
			break;
		case 61:
			rtn = "Australia";
			break;
		case 43:
			rtn = "Austria";
			break;
		case 994:
			rtn = "Azerbaijan";
			break;
		case 1242:
			rtn = "Bahamas";
			break;
		case 973:
			rtn = "Bahrain";
			break;
		case 880:
			rtn = "Bangladesh";
			break;
		case 1246:
			rtn = "Barbados";
			break;
		case 375:
			rtn = "Belarus";
			break;
		case 32:
			rtn = "Belgium";
			break;
		case 501:
			rtn = "Belize";
			break;
		case 229:
			rtn = "Benin";
			break;
		case 1441:
			rtn = "Bermuda Is.";
			break;
		case 591:
			rtn = "Bolivia";
			break;
		case 267:
			rtn = "Botswana";
			break;
		case 55:
			rtn = "Brazil";
			break;
		case 673:
			rtn = "Brunei";
			break;
		case 359:
			rtn = "Bulgaria";
			break;
		case 226:
			rtn = "Burkina-faso";
			break;
		case 95:
			rtn = "Burma";
			break;
		case 257:
			rtn = "Burundi";
			break;
		case 237:
			rtn = "Cameroon";
			break;

		case 1345:
			rtn = "Cayman Is.";
			break;
		case 236:
			rtn = "Central African Republic";
			break;
		case 235:
			rtn = "Chad";
			break;
		case 56:
			rtn = "Chile";
			break;
		case 86:
			rtn = "China";
			break;
		case 57:
			rtn = "Colombia";
			break;
		case 242:
			rtn = "Congo";
			break;
		case 682:
			rtn = "Cook Is.";
			break;
		case 506:
			rtn = "Costa Rica";
			break;
		case 53:
			rtn = "Cuba";
			break;
		case 357:
			rtn = "Cyprus";
			break;
		case 420:
			rtn = "Czech Republic";
			break;
		case 45:
			rtn = "Denmark";
			break;
		case 253:
			rtn = "Djibouti";
			break;
		case 1890:
			rtn = "Dominica Rep.";
			break;
		case 593:
			rtn = "Ecuador";
			break;
		case 20:
			rtn = "Egypt";
			break;
		case 503:
			rtn = "EI Salvador";
			break;
		case 372:
			rtn = "Estonia";
			break;
		case 251:
			rtn = "Ethiopia";
			break;
		case 679:
			rtn = "Fiji";
			break;
		case 358:
			rtn = "Finland";
			break;
		case 33:
			rtn = "France";
			break;
		case 594:
			rtn = "French Guiana";
			break;
		case 241:
			rtn = "Gabon";
			break;
		case 220:
			rtn = "Gambia";
			break;
		case 995:
			rtn = "Georgia";
			break;
		case 49:
			rtn = "Germany";
			break;

		case 350:
			rtn = "Gibraltar";
			break;
		case 30:
			rtn = "Greece";
			break;

		case 1671:
			rtn = "Guam";
			break;
		case 502:
			rtn = "Guatemala";
			break;
		case 224:
			rtn = "Guinea";
			break;
		case 592:
			rtn = "Guyana";
			break;
		case 509:
			rtn = "Haiti";
			break;
		case 504:
			rtn = "Honduras";
			break;
		case 852:
			rtn = "Hongkong";
			break;
		case 36:
			rtn = "Hungary";
			break;
		case 354:
			rtn = "Iceland";
			break;
		case 91:
			rtn = "India";
			break;
		case 62:
			rtn = "Indonesia";
			break;
		case 98:
			rtn = "Iran";
			break;
		case 964:
			rtn = "Iraq";
			break;
		case 353:
			rtn = "Ireland";
			break;
		case 972:
			rtn = "Israel";
			break;
		case 39:
			rtn = "Italy";
			break;
		case 225:
			rtn = "Ivory Coast";
			break;
		case 1876:
			rtn = "Jamaica";
			break;
		case 81:
			rtn = "Japan";
			break;
		case 962:
			rtn = "Jordan";
			break;
		case 855:
			rtn = "Kampuchea (Cambodia )";
			break;
		case 327:
			rtn = "Kazakstan";
			break;
		case 254:
			rtn = "Kenya";
			break;
		case 82:
			rtn = "Korea";
			break;
		case 965:
			rtn = "Kuwait";
			break;
		case 331:
			rtn = "Kyrgyzstan";
			break;
		case 856:
			rtn = "Laos";
			break;
		case 371:
			rtn = "Latvia";
			break;
		case 961:
			rtn = "Lebanon";
			break;
		case 266:
			rtn = "Lesotho";
			break;
		case 231:
			rtn = "Liberia";
			break;
		case 218:
			rtn = "Libya";
			break;
		case 423:
			rtn = "Liechtenstein";
			break;
		case 370:
			rtn = "Lithuania";
			break;
		case 352:
			rtn = "Luxembourg";
			break;
		case 853:
			rtn = "Macao";
			break;
		case 261:
			rtn = "Madagascar";
			break;
		case 265:
			rtn = "Malawi";
			break;
		case 60:
			rtn = "Malaysia";
			break;
		case 960:
			rtn = "Maldives";
			break;
		case 223:
			rtn = "Mali";
			break;
		case 356:
			rtn = "Malta";
			break;
		case 1670:
			rtn = "Mariana Is";
			break;
		case 596:
			rtn = "Martinique";
			break;
		case 230:
			rtn = "Mauritius";
			break;
		case 52:
			rtn = "Mexico";
			break;
		case 373:
			rtn = "Moldova, Republic of";
			break;
		case 377:
			rtn = "Monaco";
			break;
		case 976:
			rtn = "Mongolia";
			break;
		case 1664:
			rtn = "Montserrat Is";
			break;
		case 212:
			rtn = "Morocco";
			break;
		case 258:
			rtn = "Mozambique";
			break;
		case 264:
			rtn = "Namibia";
			break;
		case 674:
			rtn = "Nauru";
			break;
		case 977:
			rtn = "Nepal";
			break;
		case 599:
			rtn = "Netheriands Antilles";
			break;
		case 31:
			rtn = "Netherlands";
			break;
		case 64:
			rtn = "New Zealand";
			break;
		case 505:
			rtn = "Nicaragua";
			break;
		case 227:
			rtn = "Niger";
			break;
		case 234:
			rtn = "Nigeria";
			break;
		case 850:
			rtn = "North Korea";
			break;
		case 47:
			rtn = "Norway";
			break;
		case 968:
			rtn = "Oman";
			break;
		case 92:
			rtn = "Pakistan";
			break;
		case 507:
			rtn = "Panama";
			break;
		case 675:
			rtn = "Papua New Cuinea";
			break;
		case 595:
			rtn = "Paraguay";
			break;
		case 51:
			rtn = "Peru";
			break;
		case 63:
			rtn = "Philippines";
			break;
		case 48:
			rtn = "Poland";
			break;
		case 689:
			rtn = "French Polynesia";
			break;
		case 351:
			rtn = "Portugal";
			break;
		case 1787:
			rtn = "Puerto Rico";
			break;
		case 974:
			rtn = "Qatar";
			break;
		case 262:
			rtn = "Reunion";
			break;
		case 40:
			rtn = "Romania";
			break;
		case 7:
			rtn = "Russia";
			break;
		case 1784:
			rtn = "Saint Vincent/St.Vincent";
			break;
		case 684:
			rtn = "Samoa Eastern";
			break;
		case 685:
			rtn = "Samoa Western";
			break;
		case 378:
			rtn = "San Marino";
			break;
		case 239:
			rtn = "Sao Tome and Principe";
			break;
		case 966:
			rtn = "Saudi Arabia";
			break;
		case 221:
			rtn = "Senegal";
			break;
		case 248:
			rtn = "Seychelles";
			break;
		case 232:
			rtn = "Sierra Leone";
			break;
		case 65:
			rtn = "Singapore";
			break;
		case 421:
			rtn = "Slovakia";
			break;
		case 386:
			rtn = "Slovenia";
			break;
		case 677:
			rtn = "Solomon Is";
			break;
		case 252:
			rtn = "Somali";
			break;
		case 27:
			rtn = "South Africa";
			break;
		case 34:
			rtn = "Spain";
			break;
		case 94:
			rtn = "Sri Lanka";
			break;
		case 1758:
			rtn = "St.Lucia/Saint Lueia";
			break;

		case 249:
			rtn = "Sudan";
			break;
		case 597:
			rtn = "Suriname";
			break;
		case 268:
			rtn = "Swaziland";
			break;
		case 46:
			rtn = "Sweden";
			break;
		case 41:
			rtn = "Switzerland";
			break;
		case 963:
			rtn = "Syria";
			break;
		case 886:
			rtn = "Taiwan";
			break;
		case 992:
			rtn = "Tajikstan";
			break;
		case 255:
			rtn = "Tanzania";
			break;
		case 66:
			rtn = "Thailand";
			break;
		case 228:
			rtn = "Togo";
			break;
		case 676:
			rtn = "Tonga";
			break;
		case 1809:
			rtn = "Grenada/Trinidad and Tobago";
			break;
		case 216:
			rtn = "Tunisia";
			break;
		case 90:
			rtn = "Turkey";
			break;
		case 993:
			rtn = "Turkmenistan";
			break;
		case 256:
			rtn = "Uganda";
			break;
		case 380:
			rtn = "Ukraine";
			break;
		case 971:
			rtn = "United Arab Emirates";
			break;
		case 44:
			rtn = "United Kiongdom";
			break;
		case 1:
			rtn = "Canada/United States of America";
			break;
		case 598:
			rtn = "Uruguay";
			break;
		case 233:
			rtn = "Ghana/Uzbekistan";
			break;
		case 58:
			rtn = "Venezuela";
			break;
		case 84:
			rtn = "Vietnam";
			break;
		case 967:
			rtn = "Yemen";
			break;
		case 381:
			rtn = "Yugoslavia";
			break;
		case 263:
			rtn = "Zimbabwe";
			break;
		case 243:
			rtn = "Zaire";
			break;
		case 260:
			rtn = "Zambia";
			break;
		default:
			rtn = "Other country";
		}

		return rtn;
	}
}
