import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LocalAssociationProvinceDetailComponent } from './local-association-province-detail.component';

describe('LocalAssociationProvince Management Detail Component', () => {
  let comp: LocalAssociationProvinceDetailComponent;
  let fixture: ComponentFixture<LocalAssociationProvinceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LocalAssociationProvinceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ localAssociationProvince: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LocalAssociationProvinceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LocalAssociationProvinceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load localAssociationProvince on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.localAssociationProvince).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
