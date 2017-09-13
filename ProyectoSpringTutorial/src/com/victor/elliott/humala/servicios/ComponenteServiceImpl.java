package com.victor.elliott.humala.servicios;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.victor.elliott.humala.formulario.ComponenteForm;

@Service
public class ComponenteServiceImpl implements ComponenteService {
	private static List<ComponenteForm> listaComponentes=new ArrayList<ComponenteForm>();
	@Override
	public void addComponente(ComponenteForm componente) {
		listaComponentes.add(componente);
	}

	@Override
	public List<ComponenteForm> getComponente() {
		return listaComponentes;
	}
}
