import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SystemConfigurationDetailComponent } from './system-configuration-detail.component';

describe('SystemConfiguration Management Detail Component', () => {
  let comp: SystemConfigurationDetailComponent;
  let fixture: ComponentFixture<SystemConfigurationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SystemConfigurationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ systemConfiguration: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SystemConfigurationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SystemConfigurationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load systemConfiguration on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.systemConfiguration).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
