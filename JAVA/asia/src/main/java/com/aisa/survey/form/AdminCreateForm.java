package com.aisa.survey.form;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminCreateForm {
	
	@Size(min=3, max=25)
	@NotEmpty(message= "ID는 필수항목입니다.")
	private String name;
	
	@NotEmpty(message= "PASSWORD는 필수항목입니다.")
	private String password1;
	
	@NotEmpty(message= "CONFIRM PASSWORD는 필수항목입니다.")
	private String password2;

	@NotEmpty(message= "ADMIN KEY를 입력해주세요.")
	private String keyCode;

}
