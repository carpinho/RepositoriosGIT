package com.victor.elliott.humala.servicios;

import com.victor.elliott.humala.formulario.ComponenteForm;
import java.util.List;

public interface ComponenteService {
	public void addComponente(ComponenteForm componente);
	public List<ComponenteForm> getComponente();
}