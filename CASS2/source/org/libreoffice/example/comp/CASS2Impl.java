package org.libreoffice.example.comp;

import com.sun.star.uno.XComponentContext;

import cass.libreOffice.LibreOfficeCass;
import cass.libreOffice.WSD_Result;

import com.sun.star.lib.uno.helper.Factory;

import com.sun.star.lang.XSingleComponentFactory;
import com.sun.star.registry.XRegistryKey;
import com.sun.star.lib.uno.helper.WeakBase;


public final class CASS2Impl extends WeakBase
   implements com.sun.star.lang.XServiceInfo,
              org.libreoffice.example.XCASS2
{
    private final XComponentContext m_xContext;
    private static final String m_implementationName = CASS2Impl.class.getName();
    private static final String[] m_serviceNames = {
        "org.libreoffice.example.CASS2" };

    private int synonymCount;
    private int synsetCount;
    private String[][] synonyms;
    
    public String getSyn(int i, int j) {
    	return synonyms[i][j];
    }

    public int getsynonymCount() {
		return synonymCount;
	}

	public void setsynonymCount(int synonymCount) {
		this.synonymCount = synonymCount;
	}

	public int getsynsetCount() {
		return synsetCount;
	}

	public void setsynsetCount(int synsetCount) {
		this.synsetCount = synsetCount;
	}
	
    public void getSynonym(String leftContext, String target, String rightContext, String language, String algorithm) {
    	LibreOfficeCass loc = new LibreOfficeCass(leftContext, target, rightContext, language);
    	WSD_Result result = loc.getSynonyms(algorithm);
    	
    	synonymCount = result.SynonymCount;
    	synsetCount = result.SynsetCount;
    	synonyms = result.Synonyms;
	}


    public CASS2Impl( XComponentContext context )
    {
        m_xContext = context;
    };

    public static XSingleComponentFactory __getComponentFactory( String sImplementationName ) {
        XSingleComponentFactory xFactory = null;

        if ( sImplementationName.equals( m_implementationName ) )
            xFactory = Factory.createComponentFactory(CASS2Impl.class, m_serviceNames);
        return xFactory;
    }

    public static boolean __writeRegistryServiceInfo( XRegistryKey xRegistryKey ) {
        return Factory.writeRegistryServiceInfo(m_implementationName,
                                                m_serviceNames,
                                                xRegistryKey);
    }

    // com.sun.star.lang.XServiceInfo:
    public String getImplementationName() {
         return m_implementationName;
    }

    public boolean supportsService( String sService ) {
        int len = m_serviceNames.length;

        for( int i=0; i < len; i++) {
            if (sService.equals(m_serviceNames[i]))
                return true;
        }
        return false;
    }

    public String[] getSupportedServiceNames() {
        return m_serviceNames;
    }
}
