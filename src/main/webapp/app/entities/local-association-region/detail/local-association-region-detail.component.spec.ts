import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LocalAssociationRegionDetailComponent } from './local-association-region-detail.component';

describe('LocalAssociationRegion Management Detail Component', () => {
  let comp: LocalAssociationRegionDetailComponent;
  let fixture: ComponentFixture<LocalAssociationRegionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LocalAssociationRegionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ localAssociationRegion: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LocalAssociationRegionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LocalAssociationRegionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load localAssociationRegion on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.localAssociationRegion).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
