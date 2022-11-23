import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AssociationDetailComponent } from './association-detail.component';

describe('Association Management Detail Component', () => {
  let comp: AssociationDetailComponent;
  let fixture: ComponentFixture<AssociationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AssociationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ association: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AssociationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AssociationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load association on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.association).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
